CREATE DATABASE IF NOT EXISTS learn
  DEFAULT CHARACTER SET = utf8mb4;
DROP TABLE IF EXISTS `Users`;


Users	CREATE TABLE `Users` (
  `Id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增Id',
  `Username` varchar(64) NOT NULL DEFAULT 'default' COMMENT '用户名',
  `Password` varchar(64) NOT NULL DEFAULT 'default' COMMENT '密码',
  `Email` varchar(64) NOT NULL DEFAULT 'default' COMMENT '邮箱地址',
  `Enabled` tinyint(4) NOT NULL DEFAULT '1' COMMENT '是否有效',
  `Role` varchar(32) NOT NULL DEFAULT '',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表'

INSERT INTO `Users` (`Username`, `Password`, `Email`, `Enabled`, `Role`)
VALUES
  ('learn', '$2a$10$TTXd9kv6tlwEk5x0ZLgnGu7/c18MJv3nHP36IKo7y.FuwNctizbLW
', 'laujunbupt0916@163.com', 1,'ROLE_ADMIN');