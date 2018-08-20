package cn.jiiiiiin.security.web.controller;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// 使用spring runner执行测试用例
@RunWith(SpringRunner.class)
// 声明为spring boot的测试用例
@SpringBootTest
public class UserControllerTest {

    final static Logger L = LoggerFactory.getLogger(UserControllerTest.class);

    @Autowired
    private WebApplicationContext context;

    // 需要伪造一个web环节，即不启动tomcat完成测试
    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        // 构建mvc环境
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    /**
     * 返回400标识请求格式不符合服务端要求（如，没有传递限定要求必传的参数等）
     *
     * @throws Exception
     */
    @Test
    public void whenQrySuccess() throws Exception {
        // mockMvc.perform() 表示需要执行一个请求
        // MockMvcRequestBuilders.get() 表示需要构建一个get请求
        // contentType(MediaType.APPLICATION_JSON_UTF8) 请求的内容类型
        final String res = mockMvc.perform(get("/user")
                // .param("","") 可以通过该方法设置参数, org.springframework.data.domain.Pageable
                .param("username", "tom")
                .param("age", "10")
                .param("ageTo", "18")
                // 设置分页参数：
                // size每页查多少条->pageable.getPageSize();
                .param("size", "10")
                // page从第几页开始查询->pageable.getPageNumber();
                .param("page", "3")
                // 查询的排序：按照年龄的降序->pageable.getSort();
                .param("sort", "age,desc")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                // .andExpect() 编写断言（期望服务器端返回的数据）
                // MockMvcResultMatchers.status().isOk() 表示期望请求是成功的
                .andExpect(status().isOk())
                // MockMvcResultMatchers.jsonPath() 使用这个方法来解析返回的json数据内容，对内容进行断言
                // 期望返回3条数据，"$.length()"表示认为返回的数据是一个集合，且长度是3，$代表查询得到的根元素，那么在这里就是一个数组对象
                // https://github.com/json-path/JsonPath 文档
                .andExpect(jsonPath("$.length()").value(3))
                // 将返回的结果一字符串形式获取
                .andReturn().getResponse().getContentAsString();
        L.info("qry user list {}", res);
    }

    @Test
    public void whenQryUserInfoSuccess() throws Exception {
        final String res = mockMvc.perform(get("/user/1").contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("tom"))
                .andReturn().getResponse().getContentAsString();
        L.info("qry user info {}", res);
    }

    @Test
    public void whenQryUserInfoFial() throws Exception {
        mockMvc.perform(get("/user/a").contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void whenCreateSuccess() throws Exception {
        final String reqContent = "{\"username\":\"tom\",\"password\":null,\"birthday\":"+new Date().getTime()+"}";
        final String res = mockMvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON_UTF8)
                // RESTFul请求参数以json格式传递
                .content(reqContent))
                // 405 标识请求的方式（POST）后台未定义这样的接口
                // 400 标识请求的格式错误，因为后台会校验对应字段的格式，但是校验失败
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
        .andReturn().getResponse().getContentAsString();
        L.info("whenCreateSuccess res {}", res);
    }

    @After
    public void tearDown() throws Exception {
    }
}