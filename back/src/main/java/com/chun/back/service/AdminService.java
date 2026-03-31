package com.chun.back.service;

import com.chun.back.pojo.Admin;

public interface AdminService {

    /**
     * 根据用户名查询管理员信息
     * @param username 用户名
     * @return 管理员对象，如果不存在则返回 null
     */
    Admin findByUsername(String username);

    /**
     * 管理员登录验证
     * @param username 用户名
     * @param password 密码（明文）
     * @return 登录成功返回 Admin 对象，失败返回 null
     */
    Admin login(String username, String password);

    /**
     * 统计管理员总数
     * @return 管理员数量
     */
    Long countAdmins();
}
