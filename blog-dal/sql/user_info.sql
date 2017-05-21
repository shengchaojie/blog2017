CREATE TABLE `user_info` (
   `id` int(11) NOT NULL AUTO_INCREMENT,
   `age` int(11) DEFAULT NULL,
   `birth` datetime DEFAULT NULL,
   `gender` int(11) DEFAULT NULL,
   `user_id` int(11) NOT NULL,
   `nick_name` varchar(255) DEFAULT null,
   `create_person` varchar(255) DEFAULT 'system',
   `create_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   `update_person` varchar(255) DEFAULT 'system',
   `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   PRIMARY KEY (`id`),
   KEY `FKn8pl63y4abe7n0ls6topbqjh2` (`user_id`),
   UNIQUE KEY `user_id_UNIQUE` (`user_id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8;