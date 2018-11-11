package cn.jiiiiiin.module.common.mapper.mngauth;


import cn.jiiiiiin.ManagerApp;
import cn.jiiiiiin.module.common.dto.mngauth.Menu;
import cn.jiiiiiin.module.common.entity.mngauth.Resource;
import cn.jiiiiiin.module.common.entity.mngauth.Role;
import cn.jiiiiiin.security.rbac.component.dict.RbacDict;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlHelper;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.annotation.Transient;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

import static cn.jiiiiiin.security.core.dict.CommonConstants.GET;
import static cn.jiiiiiin.security.core.dict.CommonConstants.POST;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ManagerApp.class)
@Slf4j
public class ResourceMapperTest {

    @Autowired
    private ResourceMapper resourceMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
//    @Transactional
//    @Rollback
    public void testInsert() {
        val resource = new Resource()
                .setName("控制台")
                .setLevels(1)
                .setUrl("/")
                .setNum(1)
                .setMethod(GET);
        int res = resourceMapper.insert(resource);
        Assert.assertTrue(SqlHelper.retBool(res));
        Assert.assertTrue(resource.getId() > 0);
        log.info("admin res {}", resource);
//
//        val resource2 = new Resource()
//                .setName("添加用户")
//                .setPid(resource.getId())
//                .setLevels(2)
//                .setNum(1)
//                .setUrl("/admin")
//                .setMethod(POST);
//        int res2 = resourceMapper.insert(resource2);
//        Assert.assertTrue(SqlHelper.retBool(res2));

        val operator = roleMapper.selectOne(new QueryWrapper<Role>().eq(Role.AUTHORITY_NAME, "OPERATOR"));
        Set<Resource> set = new HashSet<>();
        set.add(resource);
//        set.add(resource2);
        operator.setResources(set);
        int res3 = roleMapper.relationResource(operator);
        Assert.assertTrue(SqlHelper.retBool(res3));
    }

    @Test
    public void testSelectByRoleId() {
        val operator = roleMapper.selectOne(new QueryWrapper<Role>().eq(Role.AUTHORITY_NAME, "OPERATOR"));
        Assert.assertNotNull(operator);
        val res = resourceMapper.selectByRoleId(operator.getId(), 1);
        log.info("testSelectByRoleId {}", res);
        Assert.assertNotNull(res);
        Assert.assertTrue(res.size() > 0);
    }

    @Test
    public void testModelMapper() {
        val modelMapper = new ModelMapper();
        val resource = new Resource()
                .setName("控制台")
                .setIcon("home")
                .setLevels(1)
                .setUrl("/")
                .setNum(1)
                .setMethod(GET);
        modelMapper.addMappings(new PropertyMap<Resource, Menu>() {

            @Override
            protected void configure() {
                map().setPath(source.getUrl());
                map().setTitle(source.getName());
            }
        });
        val menu = modelMapper.map(resource, Menu.class);
        log.info("menu {}", menu);
    }
}
