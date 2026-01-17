package com.sky.util;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 用户上下文工具类
 * 用于在Controller中获取当前登录用户信息
 */
public class UserContext {

    /**
     * 从request中获取当前用户ID
     * @param request HttpServletRequest
     * @return 用户ID
     */
    public static Long getUserId(HttpServletRequest request) {
        Object userId = request.getAttribute("userId");
        if (userId instanceof Long) {
            return (Long) userId;
        } else if (userId instanceof Integer) {
            return ((Integer) userId).longValue();
        }
        return null;
    }

    /**
     * 从request中获取当前用户名
     * @param request HttpServletRequest
     * @return 用户名
     */
    public static String getUsername(HttpServletRequest request) {
        return (String) request.getAttribute("username");
    }

    /**
     * 从request中获取当前用户类型
     * @param request HttpServletRequest
     * @return 用户类型：user-普通用户, admin-管理员
     */
    public static String getUserType(HttpServletRequest request) {
        return (String) request.getAttribute("userType");
    }

    /**
     * 从request中获取当前token
     * @param request HttpServletRequest
     * @return JWT token
     */
    public static String getToken(HttpServletRequest request) {
        return (String) request.getAttribute("token");
    }

    /**
     * 判断当前用户是否为管理员
     * @param request HttpServletRequest
     * @return 是否为管理员
     */
    public static boolean isAdmin(HttpServletRequest request) {
        return "admin".equals(getUserType(request));
    }
}

