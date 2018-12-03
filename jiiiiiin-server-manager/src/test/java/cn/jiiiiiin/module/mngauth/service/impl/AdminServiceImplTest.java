package cn.jiiiiiin.module.mngauth.service.impl;

import cn.jiiiiiin.ManagerApp;
import cn.jiiiiiin.module.common.entity.mngauth.Admin;
import cn.jiiiiiin.module.common.entity.mngauth.Role;
import cn.jiiiiiin.module.common.enums.common.ChannelEnum;
import cn.jiiiiiin.module.common.mapper.mngauth.RoleMapper;
import cn.jiiiiiin.module.mngauth.service.IAdminService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.val;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertTrue;


/**
 * 测试Service
 * https://mrbird.cc/Spring-Boot%20TESTing.html
 */
// 使用spring runner执行测试用例
@RunWith(SpringRunner.class)
// 声明为spring boot的测试用例
@SpringBootTest(classes = ManagerApp.class)
public class AdminServiceImplTest {

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private IAdminService adminService;

    @Autowired
    private RoleMapper roleMapper;

    @Transactional
    @Test
    public void findByUsername() {
        new Admin().setUsername("TEMP").setPassword("$2a$10$XQi3SDI8aU8VL8PQkkyddOYk62OmDBtLwD9f9EEKf0AZBI0Y7pwPq").setChannel(ChannelEnum.MNG);
        val admin = adminService.signInByUsername("TEMP", ChannelEnum.MNG);
        Assert.assertEquals("TEMP", admin.getUsername());
        // 测试EHCache缓存
//        val admin2 = adminService.signInByUsername("user", ChannelEnum.MNG);
    }


    @Transactional
    @Test
    public void saveRelationRoleRecords() {
        Set<Role> roles = new HashSet<>();
        val adminRole = roleMapper.selectOne(new QueryWrapper<Role>().eq(Role.AUTHORITY_NAME, "ADMIN"));
        val userRole = roleMapper.selectOne(new QueryWrapper<Role>().eq(Role.AUTHORITY_NAME, "DB_ADMIN"));
        roles.add(adminRole);
        roles.add(userRole);
        val admin = adminService.signInByUsername("admin", ChannelEnum.MNG);
        admin.setRoles(roles);
        boolean res = adminService.saveRelationRoleRecords(admin);
        assertTrue(res);
    }

    @Test
    public void saveAdminAndRelationRecords() {
        val admin = new Admin().setUsername("User1").setPassword("$2a$10$XQi3SDI8aU8VL8PQkkyddOYk62OmDBtLwD9f9EEKf0AZBI0Y7pwPq").setChannel(ChannelEnum.MNG);
        admin.getRoles().add(roleMapper.selectById("1061277220292595713"));
        admin.getRoles().add(roleMapper.selectById("1061277221798350849"));
        val res = adminService.saveAdminAndRelationRecords(admin);
        assertTrue(res);
    }

    @Test
    public void updateAdminAndRelationRecords() {
        val admin = new Admin().setId(1069587774891364354L).selectById();
        admin.setUsername("User")
                .setPhone("153989999999")
                .setEmail("153989999999@163.com");
        admin.getRoles().add(roleMapper.selectById("1061277220292595713"));
        val res = adminService.updateAdminAndRelationRecords(admin);
        assertTrue(res);
    }

    @Transactional
    @Test
    public void delAdminAndRelationRecords() {
        val res = adminService.delAdminAndRelationRecords(1069587774891364354L, ChannelEnum.MNG);
        assertTrue(res);
    }

}