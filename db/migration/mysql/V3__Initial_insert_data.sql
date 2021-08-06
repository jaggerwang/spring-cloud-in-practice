SET NAMES utf8mb4;

USE `scip_user`;

-- ----------------------------
-- Records of user
-- ----------------------------
BEGIN;
INSERT INTO `user` VALUES (1, 'jaggerwang', '$2a$10$M4c3efK.JQ0OI1ZLoDtkueJw501GUFE3/fZHDf9umGKmiRmZ5by0m', '18600000000', NULL, NULL, '', '2020-01-31 01:24:24', '2020-03-05 17:34:26');
COMMIT;

-- ----------------------------
-- Records of role
-- ----------------------------
BEGIN;
INSERT INTO `role` VALUES (1, 'user', '2020-03-28 03:16:10', NULL);
INSERT INTO `role` VALUES (2, 'post', '2020-03-28 03:16:10', NULL);
INSERT INTO `role` VALUES (3, 'file', '2020-03-28 03:16:10', NULL);
INSERT INTO `role` VALUES (4, 'stat', '2020-03-28 03:16:10', NULL);
COMMIT;

-- ----------------------------
-- Records of user_role
-- ----------------------------
BEGIN;
INSERT INTO `user_role` VALUES (1, 1, 1, '2020-03-28 03:16:10', NULL);
INSERT INTO `user_role` VALUES (2, 1, 2, '2020-03-28 03:16:10', NULL);
INSERT INTO `user_role` VALUES (3, 1, 3, '2020-03-28 03:16:10', NULL);
INSERT INTO `user_role` VALUES (4, 1, 4, '2020-03-28 03:16:10', NULL);
COMMIT;

-- ----------------------------
-- Records of user_follow
-- ----------------------------
BEGIN;
INSERT INTO `user_follow` VALUES (1, 1, 1, '2020-03-28 03:16:10', NULL);
COMMIT;

USE `scip_post`;

-- ----------------------------
-- Records of post
-- ----------------------------
BEGIN;
INSERT INTO `post` VALUES (1, 1, 'TEXT', 'hah', NULL, NULL, '2020-01-31 01:27:49', NULL);
INSERT INTO `post` VALUES (2, 1, 'IMAGE', 'heheh', '[1]', NULL, '2020-01-31 01:37:50', NULL);
INSERT INTO `post` VALUES (3, 1, 'IMAGE', 'sdfsadf', '[2]', NULL, '2020-02-23 03:20:05', NULL);
COMMIT;

-- ----------------------------
-- Records of post_like
-- ----------------------------
BEGIN;
INSERT INTO `post_like` VALUES (1, 1, 1, '2020-01-31 01:45:36', NULL);
INSERT INTO `post_like` VALUES (2, 2, 1, '2020-02-23 03:19:46', NULL);

USE `scip_file`;

-- ----------------------------
-- Records of file
-- ----------------------------
BEGIN;
INSERT INTO `file` VALUES (1, 1, 'LOCAL', '', 'post/5e33d94d9413cb27cb8d6e78.jpg', '{\"name\": \"image_picker_AB3DED88-7BBC-4FB1-8BDD-5102888CE14E-8570-00000FFA0FE1C0BC.jpg\", \"size\": 1930990, \"type\": \"image/jpeg\"}', '2020-01-31 01:37:50', NULL);
INSERT INTO `file` VALUES (2, 1, 'LOCAL', '', 'post/5e5243c44eb5fd474ee0491d.jpg', '{\"name\": \"image_picker_BEF29478-BD87-4E5B-90DB-BE5C0DF79995-88877-0001CDE3833C672B.jpg\", \"size\": 1857368, \"type\": \"image/jpeg\"}', '2020-02-23 03:20:05', NULL);
COMMIT;

USE `scip_stat`;

-- ----------------------------
-- Records of post_stat
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Records of user_stat
-- ----------------------------
BEGIN;
COMMIT;
