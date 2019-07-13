CREATE DATABASE IF NOT EXISTS learn
  DEFAULT CHARACTER SET = utf8mb4;
DROP TABLE IF EXISTS `Users`;

CREATE TABLE `Users` (
  `Id`       int(10) unsigned NOT NULL AUTO_INCREMENT
  COMMENT '自增Id',
  `Username` varchar(64)      NOT NULL DEFAULT 'default'
  COMMENT '用户名',
  `Password` varchar(64)      NOT NULL DEFAULT 'default'
  COMMENT '密码',
  `Email`    varchar(64)      NOT NULL DEFAULT 'default'
  COMMENT '邮箱地址',
  `Enabled`  tinyint(4)                DEFAULT NULL
  COMMENT '是否有效',
  PRIMARY KEY (`Id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COMMENT ='用户表';