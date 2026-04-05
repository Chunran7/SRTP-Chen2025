package com.chun.back.pojo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Introduction {
    private Integer id;
    private String title;
    private String content;
    private String imageUrl;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
