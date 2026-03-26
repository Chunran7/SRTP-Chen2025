-- 创建管理员表
CREATE TABLE `admin` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '管理员 ID',
  `username` VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
  `password_hash` VARCHAR(255) NOT NULL COMMENT '密码哈希值',
  `nickname` VARCHAR(50) DEFAULT NULL COMMENT '昵称',
  `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
  `status` TINYINT DEFAULT 1 COMMENT '状态（0:禁用，1:正常）',
  `admin_pic` VARCHAR(255) DEFAULT NULL COMMENT '头像 URL',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_username` (`username`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员表';

-- 插入默认管理员账号
-- 用户名：seu666
-- 密码：yilutongxing (MD5 加密后的值)
-- 使用 MySQL 的 MD5 函数生成密码哈希
INSERT INTO `admin` (`username`, `password_hash`, `nickname`, `status`) 
VALUES ('seu666', MD5('yilutongxing'), '系统管理员', 1);
