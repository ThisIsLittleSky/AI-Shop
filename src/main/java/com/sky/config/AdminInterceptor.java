package com.sky.config;

import com.sky.annotation.RequireAdmin;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 管理员权限拦截器
 * 验证接口是否需要管理员权限
 */
@Component
public class AdminInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 如果不是方法处理器，直接放行（由JWT拦截器处理）
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        
        // 检查方法或类上是否有@RequireAdmin注解
        boolean requireAdmin = handlerMethod.hasMethodAnnotation(RequireAdmin.class) 
                || handlerMethod.getBeanType().isAnnotationPresent(RequireAdmin.class);

        if (requireAdmin) {
            // 从request属性中获取用户类型（由JwtInterceptor设置）
            String userType = (String) request.getAttribute("userType");
            
            // 验证是否为管理员
            if (userType == null || !"admin".equals(userType)) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"code\":403,\"message\":\"需要管理员权限\"}");
                return false;
            }
        }

        return true;
    }
}

