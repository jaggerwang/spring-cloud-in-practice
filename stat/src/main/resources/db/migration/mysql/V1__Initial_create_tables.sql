SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for post_stat
-- ----------------------------
DROP TABLE IF EXISTS `post_stat`;
CREATE TABLE `post_stat` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `post_id` bigint(20) NOT NULL COMMENT '动态 ID',
  `like_count` bigint(20) NOT NULL DEFAULT '0' COMMENT '被喜欢次数',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_post_id` (`post_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='动态统计';

-- ----------------------------
-- Table structure for user_stat
-- ----------------------------
DROP TABLE IF EXISTS `user_stat`;
CREATE TABLE `user_stat` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户 ID',
  `post_count` bigint(20) NOT NULL DEFAULT '0' COMMENT '发布动态数',
  `like_count` bigint(20) NOT NULL DEFAULT '0' COMMENT '喜欢动态数',
  `following_count` bigint(20) NOT NULL DEFAULT '0' COMMENT '关注人数',
  `follower_count` bigint(20) NOT NULL DEFAULT '0' COMMENT '粉丝人数',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户统计';

SET FOREIGN_KEY_CHECKS = 1;
