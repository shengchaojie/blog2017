CREATE TABLE `crawl_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `job_type` varchar(45) DEFAULT NULL COMMENT 'job的类型',
  `crawl_time` datetime DEFAULT NULL COMMENT '爬取的时间',
  `valid_duration` int(11) DEFAULT NULL COMMENT '爬取的有效时间',
  `deleted` tinyint(2) DEFAULT NULL COMMENT '是否失效',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='爬虫时间信息表';

CREATE TABLE `music_album` (
  `id` int(11) NOT NULL,
  `album_name` varchar(200) DEFAULT NULL,
  `album_url` varchar(45) DEFAULT NULL,
  `img_url` varchar(200) DEFAULT NULL,
  `singer_id` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `crawl_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `music_singer` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `singer_name` varchar(200) DEFAULT NULL,
  `singer_url` varchar(200) DEFAULT NULL,
  `img_url` varchar(45) DEFAULT NULL,
  `first_letter` varchar(45) DEFAULT NULL COMMENT '首写字母',
  `crawl_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE `music_song` (
  `id` int(11) NOT NULL,
  `song_name` varchar(200) DEFAULT NULL,
  `song_url` varchar(200) DEFAULT NULL,
  `song_download_url` varchar(200) DEFAULT NULL,
  `img_url` varchar(200) DEFAULT NULL,
  `comment_count` int(11) DEFAULT NULL,
  `singer_id` int(11) DEFAULT NULL,
  `album_id` int(11) DEFAULT NULL,
  `crawl_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `index_count` (`comment_count`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `music_song_comment` (
  `id` int(11) NOT NULL,
  `content` varchar(300) DEFAULT NULL,
  `comment_time` datetime DEFAULT NULL,
  `like_count` int(11) DEFAULT NULL,
  `username` varchar(200) DEFAULT NULL,
  `user_avatar_url` varchar(200) DEFAULT NULL,
  `crawl_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `music_webpage` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `webpage_id` int(11) DEFAULT NULL,
  `webpage_type` int(11) DEFAULT NULL,
  `webpage_content` longtext,
  `crawled` int(1) DEFAULT NULL,
  `crawl_time` datetime DEFAULT NULL,
  `webpage_index` int(11) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `index2` (`webpage_id`,`webpage_type`,`webpage_index`)
) ENGINE=InnoDB AUTO_INCREMENT=97507 DEFAULT CHARSET=utf8 COMMENT='用于缓存页面';

CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `password` varchar(255) DEFAULT NULL COMMENT '密码 md5加密',
  `username` varchar(255) DEFAULT NULL COMMENT '用户名',
  `create_person` varchar(255) DEFAULT 'system',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_person` varchar(255) DEFAULT 'system',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;


CREATE TABLE `user_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `age` int(11) DEFAULT NULL,
  `birth` datetime DEFAULT NULL,
  `gender` int(11) DEFAULT NULL,
  `user_id` int(11) NOT NULL,
  `nick_name` varchar(255) DEFAULT NULL,
  `create_person` varchar(255) DEFAULT 'system',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_person` varchar(255) DEFAULT 'system',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id_UNIQUE` (`user_id`),
  KEY `FKn8pl63y4abe7n0ls6topbqjh2` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;



