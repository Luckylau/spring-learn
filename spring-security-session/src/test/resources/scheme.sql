CREATE TABLE `users` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增Id',
  `username` varchar(64) NOT NULL DEFAULT 'default' COMMENT '用户名',
  `password` varchar(64) NOT NULL DEFAULT 'default' COMMENT '密码',
  `email` varchar(64) NOT NULL DEFAULT 'default' COMMENT '邮箱地址',
  `enabled` tinyint(4) NOT NULL DEFAULT '1' COMMENT '是否有效',
  `role` varchar(32) NOT NULL DEFAULT '',
  PRIMARY KEY (`Id`),
  UNIQUE KEY `unique_username`(`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';