truncate table mng_admin;
-- pwd def: admin
insert into mng_admin (username, password, phone, email) values ('admin', '$2a$10$XQi3SDI8aU8VL8PQkkyddOYk62OmDBtLwD9f9EEKf0AZBI0Y7pwPq', '15399999999', '15399999999@163.com');


insert into mng_resource (name, icon, url, method, num, levels, ismenu, channel) values ('首页', 'home', '/index', 'GET', 1, 1, 1, 0);

insert into mng_resource (name, icon, url, method, num, levels, ismenu, channel) values ('演示页面', 'folder-o', '/demo', 'GET', 2, 1, 1, 0);

insert into mng_resource (pid, pids, name, url, method, num, levels, ismenu, channel) values (1049640540930371587, '0,1049640540930371587', '页面 1', '/demo/page1', 'GET', 1, 2, 1, 0);

insert into mng_resource (pid, pids, name, url, method, num, levels, ismenu, channel) values (1049640540930371587, '0,1049640540930371587', '页面 2', '/demo/page2', 'GET', 2, 2, 1, 0);

insert into mng_resource (pid, pids, name, url, method, num, levels, ismenu, channel) values (1049640540930371587, '0,1049640540930371587', '页面 3', '/demo/page3', 'GET', 3, 2, 1, 0);


