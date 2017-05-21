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
   `id` int(11) NOT NULL,
   `singer_name` varchar(200) DEFAULT NULL,
   `singer_url` varchar(200) DEFAULT NULL,
   `img_url` varchar(45) DEFAULT NULL,
   `first_letter` varchar(45) DEFAULT NULL COMMENT '首写字母',
   `crawl_time` datetime DEFAULT NULL,
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
   PRIMARY KEY (`id`)
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
 ) ENGINE=InnoDB AUTO_INCREMENT=6397 DEFAULT CHARSET=utf8 COMMENT='用于缓存页面';