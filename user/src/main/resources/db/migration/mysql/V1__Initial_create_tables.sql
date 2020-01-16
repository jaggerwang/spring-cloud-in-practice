SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`
(
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `name` varchar(20) NOT NULL COMMENT '角色名',
    `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色';

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`
(
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `username` varchar(20) NOT NULL COMMENT '用户名',
    `password` varchar(64) NOT NULL COMMENT '已加密的密码',
    `mobile` char(11) DEFAULT NULL COMMENT '手机号',
    `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
    `avatar_id` bigint(20) DEFAULT NULL COMMENT '头像文件 ID',
    `intro` varchar(100) NOT NULL DEFAULT '' COMMENT '自我介绍',
    `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_username` (`username`),
    UNIQUE KEY `idx_mobile` (`mobile`),
    UNIQUE KEY `idx_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户';

DROP TABLE IF EXISTS `user_follow`;
CREATE TABLE `user_follow`
(
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `following_id` bigint(20) NOT NULL COMMENT '被关注用户 ID',
    `follower_id` bigint(20) NOT NULL COMMENT '粉丝用户 ID',
    `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_follower_id_following_id` (`follower_id`,`following_id`),
    KEY `idx_following_id_created_at` (`following_id`,`created_at`),
    KEY `idx_follower_id_created_at` (`follower_id`,`created_at`),
    CONSTRAINT `user_follow_ibfk_1` FOREIGN KEY (`following_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `user_follow_ibfk_2` FOREIGN KEY (`follower_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户关注';

DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`
(
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id` bigint(20) NOT NULL COMMENT '用户 ID',
    `role_id` bigint(20) NOT NULL COMMENT '角色 ID',
    `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_user_id_role_id` (`user_id`,`role_id`) USING BTREE,
    KEY `idx_role_id_created_at` (`role_id`,`created_at`) USING BTREE,
    KEY `idx_user_id_created_at` (`user_id`,`created_at`) USING BTREE,
    CONSTRAINT `user_role_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `user_role_ibfk_2` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色';

SET FOREIGN_KEY_CHECKS = 1;