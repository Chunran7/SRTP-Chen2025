package com.chun.back.pojo;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Admin {
    private Long id;
    private String username;
    private String password;
    private String nickname;
    private String email;
    private Integer status;
    private String adminPic;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}