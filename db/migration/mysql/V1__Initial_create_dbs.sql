CREATE DATABASE `scip_user`;
CREATE DATABASE `scip_post`;
CREATE DATABASE `scip_file`;
CREATE DATABASE `scip_stat`;

CREATE USER 'scip_user'@'%' IDENTIFIED BY '123456';
GRANT ALL PRIVILEGES ON `scip_user`.* TO 'scip_user'@'%';
CREATE USER 'scip_post'@'%' IDENTIFIED BY '123456';
GRANT ALL PRIVILEGES ON `scip_post`.* TO 'scip_post'@'%';
CREATE USER 'scip_file'@'%' IDENTIFIED BY '123456';
GRANT ALL PRIVILEGES ON `scip_file`.* TO 'scip_file'@'%';
CREATE USER 'scip_stat'@'%' IDENTIFIED BY '123456';
GRANT ALL PRIVILEGES ON `scip_stat`.* TO 'scip_stat'@'%';