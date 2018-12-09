package cn.jiiiiiin.module.mngauth.service.impl;

import cn.jiiiiiin.ManagerApp;
import cn.jiiiiiin.module.common.entity.mngauth.Role;
import cn.jiiiiiin.module.common.enums.common.ChannelEnum;
import cn.jiiiiiin.module.mngauth.service.IRoleService;
import lombok.val;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ManagerApp.class)
public class IRoleServiceTest {

    @Autowired
    private IRoleService roleService;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void save(){
        Role role = new Role().setAuthorityName("USER1").setName("用户1").setChannel(ChannelEnum.MNG);
        Long[] resourceIds = new Long[]{1066671419766624257L, 1062518178556526593L, 1061818316563202049L};
        val res = roleService.save(role, resourceIds);
        Assert.assertTrue(res);
    }

    @Transactional
    @Test
    public void remove(){
        List<Long> idList = new ArrayList<>();
        idList.add(1069200931105271810L);
        boolean res = roleService.remove(idList);
        Assert.assertTrue(res);
    }

    @Test
    public void getRoleAndRelationRecords() {
        val res = roleService.getRoleAndRelationRecords(1069853648600694786L);
        assertNotNull(res);
        assertNotNull(res.getResources());
    }

    @Test
    public void getRoleAndRelationEleUiResourceRecords() {
        val res = roleService.getRoleAndRelationEleUiResourceRecords(1061277221831905282L);
        assertNotNull(res);
        assertNotNull(res.getResources());
    }
}