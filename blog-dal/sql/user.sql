CREATE TABLE `user` (
   `id` bigint(20) NOT NULL AUTO_INCREMENT,
   `password` varchar(255) DEFAULT NULL comment '密码 md5加密',
   `username` varchar(255) DEFAULT NULL comment '用户名',
   `create_person` varchar(255) DEFAULT 'system',
   `create_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   `update_person` varchar(255) DEFAULT 'system',
   `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8;