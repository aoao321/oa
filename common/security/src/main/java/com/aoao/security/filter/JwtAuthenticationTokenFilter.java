package com.aoao.security.filter;

import com.aoao.constant.RedisKeyConstants;
import com.aoao.enums.ResponseCodeEnum;
import com.aoao.exception.BizException;
import com.aoao.mapper.SysMenuMapper;
import com.aoao.mapper.SysUserMapper;
import com.aoao.context.UserContext;
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
import org.springframework.data.redis.core.StringRedisTemplate;
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
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // 获取token
        String header = request.getHeader(tokenHeaderKey);
        if (StringUtils.startsWith(header, tokenPrefix)) {
            String token = StringUtils.substring(header, tokenPrefix.length()).trim();
            if (StringUtils.isEmpty(token)) {
                filterChain.doFilter(request, response);
                return;
            }
            // 处理异常
            try {
                jwtTokenHelper.validateToken(token);
                String username = jwtTokenHelper.getUsernameByToken(token);
                if (StringUtils.isNotBlank(username)
                        && Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
                    // 查询用户
                    SysUser selectedUser = sysUserMapper.selectOne(new QueryWrapper<SysUser>().eq("username", username));
                    if (selectedUser == null) {
                        throw new UsernameNotFoundException(username);
                    }
                    if (selectedUser.getStatus() != 1) {
                        authenticationEntryPoint.commence(request, response,
                                new AuthenticationServiceException("用户已被禁用"));
                        return;
                    }
                    // 查询redis
                    String redisKey = RedisKeyConstants.buildTokenKey(selectedUser.getId());
                    String redisToken = stringRedisTemplate.opsForValue().get(redisKey);
                    if (StringUtils.isBlank(redisToken)) {
                        authenticationEntryPoint.commence(
                                request,
                                response,
                                new AuthenticationServiceException("登录状态已失效，请重新登录")
                        );
                        return;
                    }
                    if (!token.equals(redisToken)) {
                        authenticationEntryPoint.commence(
                                request,
                                response,
                                new AuthenticationServiceException("账号已在其他地方登录")
                        );
                        return;
                    }
                    // 获取权限
                    List<String> list = sysMenuMapper.selectUserWithMenus(selectedUser.getUsername());
                    LoginUser loginUser = new LoginUser(selectedUser, list);

                    SecurityContextHolder.getContext()
                            .setAuthentication(new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities()));

                    // 这里存入ThreadLocal
                    UserContext.setUsername(username);
                }
            } catch (ExpiredJwtException e) {
                authenticationEntryPoint.commence(request, response, new AuthenticationServiceException("Token 已失效"));
                return;
            } catch (JwtException | IllegalArgumentException e) {
                authenticationEntryPoint.commence(request, response, new AuthenticationServiceException("Token 不可用"));
                return;
            }
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            // 过滤器执行完毕后清理，避免内存泄漏
            UserContext.clear();
        }
    }

}
