DROP TABLE IF EXISTS `sys_user`;

INSERT INTO `sys_user` (`id`, `create_time`, `username`, `password`, `phone`, `email`) VALUES (1, current_timestamp , '', '15388888888', 'test1@baomidou.com');


DROP TABLE IF EXISTS `sys_role`;

INSERT INTO `sys_role` (`id`, `create_time`, `name`) VALUES
(1, current_timestamp , '系统管理员'),
(2, current_timestamp , '商户管理员'),
(3, current_timestamp , '行内用户'),
(4, current_timestamp , '普通用户');

