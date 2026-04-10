package com.chun.back.interceptors;

import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.chun.back.utils.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 登录拦截器：
 * - 从请求头 Authorization: Bearer <token> 解析 JWT
 * - 将解析后的 claims 放入 request attribute "claims"，供 controller 使用
 * - 对无效或缺失 token 的请求返回 401 JSON
 * - 为了兼容公开读取接口，拦截器内部放行 GET 方法（允许公开访问）
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        // 允许所有 GET 请求通过（阅读类接口保持公开）
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        // 读取请求属性（可能已有拦截器或其他过滤器设置）
        Object attr = request.getAttribute("claims");
        if (attr instanceof Map) {
            return true;
        }

        // 从 Authorization 头解析 token
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            sendUnauthorized(response, "缺少 Authorization 头");
            return false;
        }

        String token = authHeader;
        if (authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }

        if (token == null || token.isEmpty()) {
            sendUnauthorized(response, "缺少 token");
            return false;
        }

        try {
            Map<String, Object> claims = JwtUtil.parseToken(token);
            // 将解析结果放入 request，后续 controller 可通过 request.getAttribute("claims") 获取
            request.setAttribute("claims", claims);
            return true;
        } catch (Exception e) {
            sendUnauthorized(response, "token 无效或已过期");
            return false;
        }
    }

    private void sendUnauthorized(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=utf-8");
        // 简单 JSON 响应，项目中可替换为统一 Result 序列化
        String json = String.format("{\"success\":false,\"message\":\"%s\"}", message);
        response.getWriter().write(json);
    }
}
// LoginInterceptor 已根据要求移除。
// 原先此文件实现了基于 JWT 的请求认证拦截逻辑。
// 为避免直接删除文件造成的路径/构建问题，当前保留文件作为占位注释，
// 实际逻辑已在 `WebConfig` 中取消注册，且用户登录接口也被移除。
