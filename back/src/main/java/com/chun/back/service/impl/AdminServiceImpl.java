package com.chun.back.service.impl;

import com.chun.back.mapper.AdminMapper;
import com.chun.back.pojo.Admin;
import com.chun.back.service.AdminService;
import com.chun.back.utils.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public Admin findByUsername(String username) {
        return adminMapper.findByUsername(username);
    }

    @Override
    public Admin login(String username, String password) {
        // 1. 根据用户名查询管理员
        Admin admin = adminMapper.findByUsername(username);

        // 2. 如果管理员不存在，返回 null
        if (admin == null) {
            return null;
        }

        // 3. 验证密码（使用 MD5 校验）
        // 数据库中存储的是 password，需要将输入的密码进行 MD5 加密后比对
        if (!Md5Util.checkPassword(password, admin.getPassword())) {
            return null;
        }

        // 4. 验证通过，返回管理员信息
        return admin;
    }

    @Override
    public Long countAdmins() {
        return adminMapper.countAdmins();
    }
}
