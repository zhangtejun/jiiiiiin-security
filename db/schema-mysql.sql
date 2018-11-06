-- 表名应全部大写，只包含26个大写字母和“_”。
--
-- 数据表的命名格式为：
--
-- bth：代表批量数据类（Batch）
-- mng：表示管理类表（Management）
-- hty：代表历史数据类（History）
-- rpt：表示报表类（Report）
-- sys：代表系统信息类（System）
-- temp：表示临时表（Temp）

DROP DATABASE IF EXISTS `vplusdb`;
CREATE DATABASE IF NOT EXISTS `vplusdb` DEFAULT CHARSET utf8 COLLATE utf8_general_ci;

CREATE USER 'testuser'@'localhost' IDENTIFIED BY 'test';
GRANT ALL PRIVILEGES ON vplusdb.* TO 'testuser'@'localhost';

USE `vplusdb`;

SET FOREIGN_KEY_CHECKS=0;

### 公共表结构

### 管理端结构

## jiiiiiin-module-mngauth 模块

/*Table structure for table `mng_admin` */

DROP TABLE IF EXISTS `mng_admin`;

CREATE TABLE `mng_admin` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(60) NOT NULL COMMENT '密码，加密存储',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  PRIMARY KEY (`id`),
  key idx_username(username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';

/*Table structure for table `mng_role` */

DROP TABLE IF EXISTS `mng_role`;

CREATE TABLE `mng_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `pid` bigint(20) DEFAULT 0 COMMENT '父角色id 0标识为根节点',
  `name` varchar(20) NOT NULL COMMENT '角色名称',
  `authority_name` varchar(10) NOT NULL COMMENT '角色标识',
  `num` int(11) DEFAULT NULL COMMENT '序号',
  PRIMARY KEY (`id`),
  key idx_pid(pid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色表';

/*Table structure for table `mng_role_admin` */

DROP TABLE IF EXISTS `mng_role_admin`;

CREATE TABLE `mng_role_admin` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` bigint(20) NOT NULL COMMENT '角色主键',
  `user_id` bigint(20) NOT NULL COMMENT '用户主键',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色用户关联表';

/*Table structure for table `mng_resource` */

DROP TABLE IF EXISTS `mng_resource`;

CREATE TABLE `mng_resource` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `pid` bigint(20) DEFAULT 0 COMMENT '父角色id 0标识为根节点',
  `pids` varchar(255) DEFAULT '0' COMMENT '当前菜单的所有父菜单编号',
  `name` varchar(255) DEFAULT NULL COMMENT '菜单名称',
  `url` varchar(255) NOT NULL COMMENT 'url地址',
  `method` varchar(6) NOT NULL COMMENT '接口类型，如POST标识添加',
  `num` int(65) DEFAULT NULL COMMENT '菜单排序号',
  `levels` int(65) DEFAULT NULL COMMENT '菜单层级',
  `ismenu` tinyint DEFAULT 1 COMMENT '是否是菜单（1：是  0：不是）',
  `status` tinyint DEFAULT 1 COMMENT '菜单状态 :  1:启用   0:不启用',
  `isopen` tinyint DEFAULT 0 COMMENT '是否打开:    1:打开   0:不打开',
  `channel` tinyint DEFAULT 0 COMMENT '标识渠道，不同的渠道就是不同的资源分组:    0: 后台资源 1: 前端资源',
  PRIMARY KEY (`id`),
  key idx_pid(pid),
  key idx_pids(pids)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='权限资源表';


/*Table structure for table `mng_role_resource` */
DROP TABLE IF EXISTS `mng_role_resource`;

CREATE TABLE `mng_role_resource` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` bigint(20) NOT NULL COMMENT '角色主键',
  `resource_id` bigint(20) NOT NULL COMMENT '资源主键',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色资源关联表';

/*Table structure for table `mng_resource_urls` */

### 管理端表结构

-- https://github.com/codingXiaxw/seckill/blob/master/src/main/sql/schema.sql
CREATE TABLE seckill(
  `seckill_id` BIGINT NOT NUll AUTO_INCREMENT COMMENT '商品库存ID',
  `name` VARCHAR(120) NOT NULL COMMENT '商品名称',
  `number` int NOT NULL COMMENT '库存数量',
  `start_time` TIMESTAMP  NOT NULL COMMENT '秒杀开始时间',
  `end_time`   TIMESTAMP   NOT NULL COMMENT '秒杀结束时间',
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (seckill_id),
  key idx_start_time(start_time),
  key idx_end_time(end_time),
  key idx_create_time(create_time)
)ENGINE=INNODB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT='秒杀库存表';

-- 初始化数据
INSERT into seckill(name,number,start_time,end_time)
VALUES
  ('1000元秒杀iphone6',100,'2016-01-01 00:00:00','2016-01-02 00:00:00'),
  ('800元秒杀ipad',200,'2016-01-01 00:00:00','2016-01-02 00:00:00'),
  ('6600元秒杀mac book pro',300,'2016-01-01 00:00:00','2016-01-02 00:00:00'),
  ('7000元秒杀iMac',400,'2016-01-01 00:00:00','2016-01-02 00:00:00');

-- 秒杀成功明细表
-- 用户登录认证相关信息(简化为手机号)
CREATE TABLE success_killed(
  `seckill_id` BIGINT NOT NULL COMMENT '秒杀商品ID',
  `user_phone` BIGINT NOT NULL COMMENT '用户手机号',
  `state` TINYINT NOT NULL DEFAULT -1 COMMENT '状态标识:-1:无效 0:成功 1:已付款 2:已发货',
  `create_time` TIMESTAMP NOT NULL COMMENT '创建时间',
  PRIMARY KEY(seckill_id,user_phone),/*联合主键*/
  KEY idx_create_time(create_time)
)ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='秒杀成功明细表';

### 其他表结构

# spring security 所需表结构
# [记住用户]功能所需表
DROP TABLE IF EXISTS `persistent_logins`;

create table persistent_logins (
  username  varchar(64) not null,
  series    varchar(64) primary key,
  token     varchar(64) not null,
  last_used timestamp   not null
);


# spring social 所需表结构

# This SQL contains a "create table" that can be used to create a table that JdbcUsersConnectionRepository can persist
# connection in. It is, however, not to be assumed to be production-ready, all-purpose SQL. It is merely representative
# of the kind of table that JdbcUsersConnectionRepository works with. The table and column names, as well as the general
# column types, are what is important. Specific column types and sizes that work may vary across database vendors and
# the required sizes may vary across API providers.
# [oauth2]功能所需表

DROP TABLE IF EXISTS `springsocial_UserConnection`;
create table springsocial_UserConnection (userId varchar(255) not null,
	providerId varchar(255) not null,
	providerUserId varchar(255),
	rank int not null,
	displayName varchar(255),
	profileUrl varchar(512),
	imageUrl varchar(512),
	accessToken varchar(512) not null,
	secret varchar(512),
	refreshToken varchar(512),
	expireTime bigint,
	primary key (userId, providerId, providerUserId));
create unique index UserConnectionRank on `springsocial_UserConnection`(userId, providerId, rank);


SET FOREIGN_KEY_CHECKS = 1;