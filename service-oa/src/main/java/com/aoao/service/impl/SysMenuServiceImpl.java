package com.aoao.service.impl;

import com.aoao.dto.system.AssignMenuDto;
import com.aoao.mapper.SysMenuMapper;
import com.aoao.mapper.SysRoleMenuMapper;
import com.aoao.model.system.SysMenu;
import com.aoao.model.system.SysRoleMenu;
import com.aoao.service.SysMenuService;
import com.aoao.vo.system.MetaVo;
import com.aoao.vo.system.RouterVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author aoao
 * @create 2025-07-15-14:33
 */
@Service
public class SysMenuServiceImpl implements SysMenuService {

    @Autowired
    private SysMenuMapper sysMenuMapper;
    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Override
    public List<SysMenu> findNodes() {
        List<SysMenu> sysMenus = sysMenuMapper.selectList(null);

        List<SysMenu> treeList = new ArrayList<>();
        for (SysMenu menu : sysMenus) {
            if (menu.getParentId() == 0) {
                menu.setChildren(findChildren(menu.getId(), sysMenus));
                treeList.add(menu);
            }
        }

        return treeList;
    }

    @Override
    public void save(SysMenu sysMenu) {
        sysMenuMapper.insert(sysMenu);
    }

    @Override
    public void removeById(Long id) {
        sysMenuMapper.deleteById(id);
    }

    @Override
    public void update(SysMenu sysMenu) {
        sysMenuMapper.updateById(sysMenu);
    }

    @Override
    public List<SysMenu> findSysMenuByRoleId(Long roleId) {
        // 查询角色所拥有的权限
        List<SysRoleMenu> roleMenus = sysRoleMenuMapper.selectList(new QueryWrapper<SysRoleMenu>()
                .eq("role_id", roleId));
        // 提取所有该角色已有的权限ID
        Set<Long> ownedMenuIds = roleMenus.stream()
                .map(SysRoleMenu::getMenuId)
                .collect(Collectors.toSet());

        // 查询数据库中所有权限
        List<SysMenu> sysMenus = sysMenuMapper.selectList(null);

        // 如果有权限就设置select为true
        for (SysMenu menu : sysMenus) {
            menu.setSelect(ownedMenuIds.contains(menu.getId()));
        }

        // 构建树
        List<SysMenu> treeMenuList = findChildren(0L, sysMenus);


        return treeMenuList;
    }

    @Override
    public void doAssign(AssignMenuDto assignMenuDto) {
        sysRoleMenuMapper.delete(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, assignMenuDto.getRoleId()));

        for (Long menuId : assignMenuDto.getMenuIdList()) {
            if (StringUtils.isEmpty(menuId)) continue;
            SysRoleMenu rolePermission = new SysRoleMenu();
            rolePermission.setRoleId(assignMenuDto.getRoleId());
            rolePermission.setMenuId(menuId);
            sysRoleMenuMapper.insert(rolePermission);
        }
    }


    @Override
    public List<RouterVo> findSysMenuByUserId(Long userId) {
        List<SysMenu> sysMenuList;

        if (userId == 1L) {
            // 管理员，查询所有菜单
            LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SysMenu::getStatus, 1);
            wrapper.orderByAsc(SysMenu::getSortValue);
            sysMenuList = sysMenuMapper.selectList(wrapper);
        } else {
            // 普通用户，根据userId查询其权限内菜单
            sysMenuList = sysMenuMapper.findMenuListByUserId(userId);
        }

        // 构建菜单树
        List<SysMenu> sysMenuTree = findChildren(0l, sysMenuList);
        return buildRouter(sysMenuTree); // 转为 RouterVo 列表
    }


    @Override
    public List<String> findUserPermsList(Long userId) {
        List<SysMenu> sysMenuList;

        if (userId == 1L) {
            // 管理员：获取所有菜单
            LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SysMenu::getStatus, 1);       // 启用状态
            wrapper.eq(SysMenu::getType, 2);         // 只查按钮
            sysMenuList = sysMenuMapper.selectList(wrapper);
        } else {
            // 普通用户：多表关联查询权限内菜单
            sysMenuList = sysMenuMapper.findMenuListByUserId(userId).stream()
                    .filter(menu -> menu.getType() == 2)
                    .collect(Collectors.toList());
        }
        // 提取按钮权限标识
        return sysMenuList.stream()
                .filter(menu -> !StringUtils.isEmpty(menu.getPerms()))
                .map(SysMenu::getPerms)
                .collect(Collectors.toList());
    }

    /**
     * @Override public List<SysMenu> findNodes() {
     * List<SysMenu> sysMenus = sysMenuMapper.selectList(null);
     * <p>
     * // 存储最终的根节点列表
     * List<SysMenu> treeList = new ArrayList<>();
     * <p>
     * // 创建一个 Map，key 是菜单id，value 是菜单对象
     * Map<Long, SysMenu> menuMap = new HashMap<>();
     * for (SysMenu menu : sysMenus) {
     * menuMap.put(menu.getId(), menu);
     * }
     * <p>
     * // 遍历所有菜单，构建父子关系
     * for (SysMenu menu : sysMenus) {
     * if (menu.getParentId() == 0) {
     * // 顶级菜单，加入结果列表
     * treeList.add(menu);
     * } else {
     * // 查找父节点
     * SysMenu parent = menuMap.get(menu.getParentId());
     * if (parent != null) {
     * if (parent.getChildren() == null) {
     * parent.setChildren(new ArrayList<>());
     * }
     * parent.getChildren().add(menu);
     * }
     * }
     * }
     * <p>
     * return treeList;
     * }
     */

    private List<SysMenu> findChildren(Long id, List<SysMenu> sysMenus) {
        List<SysMenu> children = new ArrayList<>();
        // 遍历sysMenus，如果id==父id
        for (SysMenu sysMenu : sysMenus) {
            // 如果是子节点
            if (sysMenu.getParentId().equals(id)) {
                // 继续递归找子节点的子节点
                sysMenu.setChildren(findChildren(sysMenu.getId(), sysMenus));
                children.add(sysMenu);
            }
        }
        return children;
    }

    private List<RouterVo> buildRouter(List<SysMenu> menus) {
        List<RouterVo> routers = new ArrayList<>();
        for (SysMenu menu : menus) {
            RouterVo router = new RouterVo();
            router.setHidden(false);
            router.setAlwaysShow(false);
            router.setPath(getRouterPath(menu));
            router.setMeta(new MetaVo(menu.getName(), menu.getIcon()));

            // 如果是目录，component 固定为 Layout
            router.setComponent(menu.getComponent()); // 设置为 Layout

            if (menu.getType() == 2){
                continue;
            }
            List<SysMenu> children = menu.getChildren();
            if (!CollectionUtils.isEmpty(children)) {
                router.setAlwaysShow(true);
                router.setChildren(buildRouter(children)); // 递归设置子菜单
            }

            routers.add(router);
        }
        return routers;
    }

    private String getRouterPath(SysMenu menu) {
        // 顶级拼接“/”
        if (menu.getParentId() == 0) {
            return "/" + menu.getPath();
        }
        return menu.getPath();
    }
}



