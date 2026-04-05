package com.chun.back.controller;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.chun.back.pojo.Result;

@RestController
@RequestMapping("/api")
public class UploadController {

    @Value("${file.upload-path}")
    private String uploadPath;

    @Value("${file.sub-paths.article:articles/}")
    private String articleSubPath;

    @Value("${file.sub-paths.content:content/}")
    private String contentSubPath;

    @Value("${file.sub-paths.introduction:introductions/}")
    private String introductionSubPath;

    @Value("${file.sub-paths.avatar:avatars/}")
    private String avatarSubPath;

    /**
     * 通用上传接口，根据 type 参数自动分类存储
     * 
     * @param file 上传的文件
     * @param type 图片类型：article(文章首图), content(内容图片), introduction(介绍图片), avatar(头像)
     */
    @PostMapping("/upload")
    public Result upload(@RequestParam("file") MultipartFile file,
            @RequestParam(value = "type", defaultValue = "content") String type) throws IOException {

        // 1. 根据类型选择子目录
        String subPath = getSubPath(type);

        // 2. 添加日期子目录（例如：2026-03/）
        String dateDir = java.time.LocalDate.now().format(
                java.time.format.DateTimeFormatter.ofPattern("yyyy-MM")) + "/";

        // 3. 生成唯一文件名，防止重名
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = UUID.randomUUID().toString() + suffix;

        // 4. 确保目录存在并保存文件
        File dest = new File(uploadPath + subPath + dateDir + fileName);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        file.transferTo(dest);

        // 5. 返回图片的可访问 URL
        String url = "http://localhost:8080/images/" + subPath + dateDir + fileName;
        return Result.success(Map.of("url", url));
    }

    /**
     * 根据类型获取对应的子路径
     */
    private String getSubPath(String type) {
        switch (type.toLowerCase()) {
            case "article":
                return articleSubPath;
            case "introduction":
                return introductionSubPath;
            case "avatar":
                return avatarSubPath;
            default:
                return contentSubPath; // 默认存到 content 目录
        }
    }
}
