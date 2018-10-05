package cn.jiiiiiin.module.mngauth.service.impl;

import cn.jiiiiiin.ManagerApp;
import cn.jiiiiiin.module.common.entity.mngauth.Admin;
import cn.jiiiiiin.module.mngauth.service.IAdminService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
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
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.test.context.junit4.SpringRunner;


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

    @Before
    public void before() {
        adminService.save(new Admin().setUsername("admin").setPassword("admin_pwd"));
    }

    @Test
    public void findByUsername() {
        val admin = adminService.findByUsername("admin");
        Assert.assertEquals("admin", admin.getUsername());
        // 测试EHCache缓存
        val admin2 = adminService.findByUsername("admin");
    }

    @After
    public void after() {
        adminService.remove(new QueryWrapper<Admin>().eq("username", "admin"));
    }
}