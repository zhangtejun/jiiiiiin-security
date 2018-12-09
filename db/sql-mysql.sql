# ************************************************************
# Sequel Pro SQL dump
# Version 4541
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 127.0.0.1 (MySQL 5.7.24)
# Database: vplusdb
# Generation Time: 2018-12-07 08:34:01 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table mng_admin
# ------------------------------------------------------------

DROP TABLE IF EXISTS `mng_admin`;

CREATE TABLE `mng_admin` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(60) NOT NULL COMMENT '密码，加密存储',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `channel` tinyint(4) DEFAULT '0' COMMENT '标识渠道，不同的渠道就是不同的资源分组: 0:内管',
  PRIMARY KEY (`id`),
  KEY `idx_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';

LOCK TABLES `mng_admin` WRITE;
/*!40000 ALTER TABLE `mng_admin` DISABLE KEYS */;

INSERT INTO `mng_admin` (`id`, `create_time`, `username`, `password`, `phone`, `email`, `channel`)
VALUES
	(1,'2018-11-10 23:12:02','admin','$2a$10$XQi3SDI8aU8VL8PQkkyddOYk62OmDBtLwD9f9EEKf0AZBI0Y7pwPq','15399999999','15399999999@163.com',0);

/*!40000 ALTER TABLE `mng_admin` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table mng_resource
# ------------------------------------------------------------

DROP TABLE IF EXISTS `mng_resource`;

CREATE TABLE `mng_resource` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `pid` bigint(20) DEFAULT '0' COMMENT '父角色id 0标识为根节点',
  `pids` varchar(255) DEFAULT '0' COMMENT '当前菜单的所有父菜单编号',
  `name` varchar(255) DEFAULT NULL COMMENT '菜单名称',
  `icon` varchar(55) DEFAULT NULL COMMENT '菜单图标',
  `num` int(65) DEFAULT NULL COMMENT '菜单排序号',
  `levels` int(65) DEFAULT '1' COMMENT '菜单层级',
  `type` tinyint(4) DEFAULT '1' COMMENT '类型: 1:菜单(默认) 0:按钮',
  `status` tinyint(4) DEFAULT '1' COMMENT '菜单状态:  1:启用   0:不启用',
  `channel` tinyint(4) DEFAULT '0' COMMENT '标识渠道，不同的渠道就是不同的资源分组: 0:内管',
  `path` varchar(255) DEFAULT NULL COMMENT '页面地址',
  PRIMARY KEY (`id`),
  KEY `idx_pid` (`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='权限资源表';

LOCK TABLES `mng_resource` WRITE;
/*!40000 ALTER TABLE `mng_resource` DISABLE KEYS */;

INSERT INTO `mng_resource` (`id`, `pid`, `pids`, `name`, `icon`, `url`, `method`, `num`, `levels`, `type`, `status`, `channel`, `path`)
VALUES
	(1061818316563202049,0,'0','权限管理','cog','','',3,1,1,1,0,'/mngauth'),
	(1061818318412890114,1061818316563202049,'0,1061818316563202049','用户管理','users','admin','GET',1,2,1,1,0,'/mngauth/admin'),
	(1061818318463221761,1061818316563202049,'0,1061818316563202049','角色管理','id-badge','role','GET',2,2,1,1,0,'/mngauth/role'),
	(1061818318517747714,1061818316563202049,'0,1061818316563202049','资源管理','tree','resource','GET',3,2,1,1,0,'/mngauth/resource'),
	(1061993676089069570,1061818318517747714,'0,1061818316563202049,1061818318517747714','新增资源',NULL,'resource','POST',2,3,0,1,0,NULL),
	(1062518178556526593,0,'0','首页','home','','GET',1,1,1,1,0,'/index'),
	(1062546326157291522,1061818318517747714,'0,1061818316563202049,1061818318517747714','修改资源',NULL,'resource','UPDATE',3,3,0,1,0,NULL),
	(1065904215529881601,1061818318517747714,'0,1061818316563202049,1061818318517747714','删除资源','','resource','DELETE',1,3,0,1,0,''),
	(1066671419766624257,0,'0','系统设置','calendar-check-o','','',2,1,1,0,0,'/mngsys'),
	(1067715924141809665,1067713955255820290,'0,1066671419766624257,1067713955255820290','test4','','','',1,3,1,1,0,'');

/*!40000 ALTER TABLE `mng_resource` ENABLE KEYS */;
UNLOCK TABLES;


DROP TABLE IF EXISTS `mng_interface`;

CREATE TABLE `mng_interface` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `url` varchar(255) NOT NULL COMMENT '接口地址',
  `name` varchar(255) DEFAULT NULL COMMENT '接口名称',
  `method` varchar(6) DEFAULT 'GET' COMMENT '接口类型，如GET标识添加',
  `desc` varchar(55) DEFAULT NULL COMMENT '接口描述',
  `status` tinyint(4) DEFAULT '1' COMMENT '菜单状态:  1:启用   0:不启用',
  `channel` tinyint(4) DEFAULT '0' COMMENT '标识渠道，不同的渠道就是不同的资源分组: 0:内管',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统接口表';


DROP TABLE IF EXISTS `mng_resource_interface`;

CREATE TABLE `mng_resource_interface` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `resource_id` bigint(20) NOT NULL COMMENT '资源主键',
  `interface_id` bigint(20) NOT NULL COMMENT '接口主键',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='资源接口关联表';


# Dump of table mng_role
# ------------------------------------------------------------

DROP TABLE IF EXISTS `mng_role`;

CREATE TABLE `mng_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(20) NOT NULL COMMENT '角色名称',
  `authority_name` varchar(10) NOT NULL COMMENT '角色标识',
  `channel` tinyint(4) NOT NULL DEFAULT '0' COMMENT '标识渠道，不同的渠道就是不同的资源分组: 0:内管',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色表';

LOCK TABLES `mng_role` WRITE;
/*!40000 ALTER TABLE `mng_role` DISABLE KEYS */;

INSERT INTO `mng_role` (`id`, `name`, `authority_name`, `channel`)
VALUES
	(1061277220292595713,'系统管理员','ADMIN',0),
	(1061277221798350849,'数据库管理员','DB_ADMIN',0),
	(1061277221831905282,'部门操作员','OPERATOR',0);

/*!40000 ALTER TABLE `mng_role` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table mng_role_admin
# ------------------------------------------------------------

DROP TABLE IF EXISTS `mng_role_admin`;

CREATE TABLE `mng_role_admin` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` bigint(20) NOT NULL COMMENT '角色主键',
  `user_id` bigint(20) NOT NULL COMMENT '用户主键',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色用户关联表';

LOCK TABLES `mng_role_admin` WRITE;
/*!40000 ALTER TABLE `mng_role_admin` DISABLE KEYS */;

INSERT INTO `mng_role_admin` (`id`, `role_id`, `user_id`)
VALUES
	(1,1061277220292595713,1);

/*!40000 ALTER TABLE `mng_role_admin` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table mng_role_resource
# ------------------------------------------------------------

DROP TABLE IF EXISTS `mng_role_resource`;

CREATE TABLE `mng_role_resource` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` bigint(20) NOT NULL COMMENT '角色主键',
  `resource_id` bigint(20) NOT NULL COMMENT '资源主键',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色资源关联表';

LOCK TABLES `mng_role_resource` WRITE;
/*!40000 ALTER TABLE `mng_role_resource` DISABLE KEYS */;

INSERT INTO `mng_role_resource` (`id`, `role_id`, `resource_id`)
VALUES
	(147,1061277221831905282,1062518178556526593),
	(159,1061277220292595713,1062518178556526593),
	(160,1061277220292595713,1066671419766624257),
	(161,1061277220292595713,1061818316563202049),
	(162,1061277220292595713,1061818318412890114),
	(163,1061277220292595713,1061818318463221761),
	(164,1061277220292595713,1061818318517747714),
	(165,1061277220292595713,1065904215529881601),
	(166,1061277220292595713,1061993676089069570),
	(167,1061277220292595713,1062546326157291522);

/*!40000 ALTER TABLE `mng_role_resource` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table mng_role_resource_for_eleui
# ------------------------------------------------------------

DROP TABLE IF EXISTS `mng_role_resource_for_eleui`;

CREATE TABLE `mng_role_resource_for_eleui` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` bigint(20) NOT NULL COMMENT '角色主键',
  `resource_ids` varchar(255) NOT NULL DEFAULT '' COMMENT 'element-ui tree选中的资源ids',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色资源element-ui树形控件选择记录关联表';

LOCK TABLES `mng_role_resource_for_eleui` WRITE;
/*!40000 ALTER TABLE `mng_role_resource_for_eleui` DISABLE KEYS */;

INSERT INTO `mng_role_resource_for_eleui` (`id`, `role_id`, `resource_ids`)
VALUES
	(17,1061277220292595713,'1062518178556526593,1066671419766624257,1061818316563202049'),
	(21,1061277221831905282,'1062518178556526593');

/*!40000 ALTER TABLE `mng_role_resource_for_eleui` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table persistent_logins
# ------------------------------------------------------------

DROP TABLE IF EXISTS `persistent_logins`;

CREATE TABLE `persistent_logins` (
  `username` varchar(64) NOT NULL,
  `series` varchar(64) NOT NULL,
  `token` varchar(64) NOT NULL,
  `last_used` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`series`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table springsocial_UserConnection
# ------------------------------------------------------------

DROP TABLE IF EXISTS `springsocial_UserConnection`;

CREATE TABLE `springsocial_UserConnection` (
  `userId` varchar(255) NOT NULL,
  `providerId` varchar(255) NOT NULL,
  `providerUserId` varchar(255) NOT NULL,
  `rank` int(11) NOT NULL,
  `displayName` varchar(255) DEFAULT NULL,
  `profileUrl` varchar(512) DEFAULT NULL,
  `imageUrl` varchar(512) DEFAULT NULL,
  `accessToken` varchar(512) NOT NULL,
  `secret` varchar(512) DEFAULT NULL,
  `refreshToken` varchar(512) DEFAULT NULL,
  `expireTime` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`userId`,`providerId`,`providerUserId`),
  UNIQUE KEY `UserConnectionRank` (`userId`,`providerId`,`rank`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;




/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
