SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for post
-- ----------------------------
DROP TABLE IF EXISTS `post`;
CREATE TABLE `post` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` bigint(20) NOT NULL COMMENT '发布者 ID',
  `type` varchar(20) NOT NULL COMMENT '类型',
  `text` varchar(100) NOT NULL DEFAULT '' COMMENT '文本内容',
  `image_ids` json DEFAULT NULL COMMENT '图片文件 ID 列表',
  `video_id` bigint(20) DEFAULT NULL COMMENT '视频文件 ID',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `video_id` (`video_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='动态';

-- ----------------------------
-- Table structure for post_like
-- ----------------------------
DROP TABLE IF EXISTS `post_like`;
CREATE TABLE `post_like` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `post_id` bigint(20) NOT NULL COMMENT '动态 ID',
  `user_id` bigint(20) NOT NULL COMMENT '点赞用户 ID',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_user_id_post_id` (`user_id`,`post_id`),
  KEY `idx_user_id_created_at` (`user_id`,`created_at`),
  KEY `idx_post_id_created_at` (`post_id`,`created_at`),
  CONSTRAINT `post_like_ibfk_1` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='动态点赞';

SET FOREIGN_KEY_CHECKS = 1;
