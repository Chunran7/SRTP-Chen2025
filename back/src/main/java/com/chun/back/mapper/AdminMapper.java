package com.chun.back.mapper;

import com.chun.back.pojo.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AdminMapper {

    /**
     * 根据用户名查询管理员信息
     * @param username 用户名
     * @return 管理员对象，如果不存在则返回 null
     */
    @Select("SELECT * FROM admin WHERE username = #{username}")
    Admin findByUsername(String username);

    /**
     * 统计管理员总数
     * @return 管理员数量
     */
    @Select("SELECT COUNT(*) FROM admin")
    Long countAdmins();
}
