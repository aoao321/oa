package com.aoao.security.filter;

import com.aoao.mapper.SysMenuMapper;
import com.aoao.mapper.SysUserMapper;
import com.aoao.model.system.SysUser;
import com.aoao.security.handler.RestAuthenticationEntryPoint;
import com.aoao.vo.system.LoginUser;
import com.aoao.utils.JwtTokenHelper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * @author aoao
 * @create 2025-07-16-10:40
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenHelper jwtTokenHelper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysMenuMapper sysMenuMapper;
    @Autowired
    private RestAuthenticationEntryPoint authenticationEntryPoint;
    @Value("${jwt.tokenPrefix}")
    private String tokenPrefix;
    @Value("${jwt.tokenHeaderKey}")
    private String tokenHeaderKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 从请求头中获取token
        String header = request.getHeader(tokenHeaderKey);

        if (StringUtils.startsWith(header, tokenPrefix)) {
            // 截取 Token 令牌
            String token = StringUtils.substring(header, 7);
            if (StringUtils.isEmpty(token)) {
                // 直接放行
                filterChain.doFilter(request, response);
                return;
            }
            // 验证token是否可用
            try {
                jwtTokenHelper.validateToken(token);
            } catch (ExpiredJwtException e) {
                authenticationEntryPoint.commence(request, response, new AuthenticationServiceException("Token 已失效"));
                return;
            } catch (JwtException | IllegalArgumentException e) {
                // 抛出异常，统一让 AuthenticationEntryPoint 处理响应参数
                authenticationEntryPoint.commence(request, response, new AuthenticationServiceException("Token 不可用"));
                return;
            }
            // 从 Token 中解析出用户名
            String username = jwtTokenHelper.getUsernameByToken(token);
            // 用户名不为空，并且SecurityContextHolder没有
            if (StringUtils.isNotBlank(username)
                    && Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
                // 从数据库中根据传入的username查询用户信息
                SysUser selectedUser = sysUserMapper.selectOne(new QueryWrapper<SysUser>()
                        .eq("username", username));
                // 判断用户是否存在
                if (selectedUser == null) {
                    throw new UsernameNotFoundException(username);
                }
                // 检查用户状态
                if (selectedUser.getStatus() != 1) {
                    authenticationEntryPoint.commence(request, response,
                            new AuthenticationServiceException("用户已被禁用"));
                    return;
                }
                // 查询用户权限
                List<String> list = sysMenuMapper.selectUserWithMenus(selectedUser.getId());
                LoginUser loginUser = new LoginUser(selectedUser, list);
                // 存入SecurityContextHolder
                SecurityContextHolder
                        .getContext()
                        .setAuthentication(new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities()));
            }
        }
        filterChain.doFilter(request, response);
    }
}
