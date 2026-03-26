package com.chun.back.controller;

import com.chun.back.pojo.Admin;
import com.chun.back.pojo.Result;
import com.chun.back.service.AdminService;
import com.chun.back.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    /**
     * 管理员登录接口
     * @param body 包含 username 和 password 的请求体
     * @return 登录结果，成功返回 token，失败返回错误信息
     */
    @PostMapping("/login")
    public Result login(@RequestBody Map<String, String> body) {
        // 1. 获取请求参数
        String username = body.get("username");
        String password = body.get("password");
        
        // 2. 校验输入格式
        if (!StringUtils.hasText(username)) {
            return Result.error("用户名不能为空");
        }
        if (!StringUtils.hasText(password)) {
            return Result.error("密码不能为空");
        }
        
        // 3. 查询管理员是否存在
        Admin admin = adminService.findByUsername(username);
        if (admin == null) {
            return Result.error("用户不存在");
        }
        
        // 4. 验证密码
        Admin loginAdmin = adminService.login(username, password);
        if (loginAdmin == null) {
            return Result.error("密码错误");
        }
        
        // 5. 生成 JWT Token
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", loginAdmin.getId());
        claims.put("username", loginAdmin.getUsername());
        String token = JwtUtil.genToken(claims);
        
        // 6. 返回登录成功及 token
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("adminInfo", loginAdmin);
        
        return Result.success(data);
    }
}
