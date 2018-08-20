package cn.jiiiiiin.security.web.controller;

import cn.jiiiiiin.security.dto.User;
import cn.jiiiiiin.security.dto.UserQryCondition;
import com.fasterxml.jackson.annotation.JsonView;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jiiiiiin
 */
// @RestController 表明需要将当前类声明为一个RESTful的api提供者
// @RequestMapping 作用是将http请求的url映射到java的方法上去
// @RequestParam 表示需要将http请求参数映射成java方法的参数
// public List<User> query(@RequestParam String username) 标识请求需要携带一个参数（GET）
// @RequestParam(name="username") String name 标识传递的参数的key和方法的参数不统一的时候，通过name或者value来进行映射
// @PathVariable 作用是将http请求的url参数映射到java的方法对应参数上去
//@GetMapping("/user/{id}")
//public User getUserInfo(@PathVariable Long id, @PathVariable(name = "id") Long idddd) {
// 使用正则表达式对 path variable进行校验@GetMapping("/user/{id:\\d+}")

// @RequestBody 映射请求体到java方法参数
// public User create(@RequestBody User user) 需要加上注解之后，才能解析前端传上来的json对象中的对应字段的值
// spring mvc直接将前端传递的参数映射到一个实体类：public List<User> query(UserQryCondition condition){

// @Valid 注解和BindingResult验证请求参数的合法性并处理校验结果
// public User create(@Valid @RequestBody User user) 如果直接这样写，那么前端如果没有按要求传递，则会直接不进入请求方法体，直接响应400 标识请求的格式错误，因为后台会校验对应字段的格式，但是校验失败
// 如果需要在校验失败也进入方法体，需要添加BindingResult到方法参数中
// BindingResult需要配置@Valid注解使用，在校验失败时候，会将错误消息（校验失败消息）映射到该对象

// @PageableDefault 用来指定分页参数的默认值
// @PageableDefault(page = 1, size = 10, sort = "username,asc") Pageable pageable
// 如果前端没有传递PageableDefault设置的默认值，那么就使用默认值映射到Pageable参数对应字段中

// @JsonView 控制接口的json输出内容
// 使用接口来声明多个视图（比如查询用户列表、和查询单用户对象）
// 如
//public interface UserSimpleView {
//}
//
///**
// * 继承 {@link User.UserSimpleView} 就能把上层接口定义的东西继承下来
// */
//public interface UserDetailView extends User.UserSimpleView {
//}
// 在值对象的get方法上指定视图
// 如：
//@JsonView(User.UserSimpleView.class)
//public String getUsername() {
//        return username;
//        }
//
//@JsonView(User.UserDetailView.class)
//public String getPassword() {
//        return password;
//        }

// 在Controller方法上指定视图

@RestController
@RequestMapping("/user")
public class UserController {

    final static Logger L = LoggerFactory.getLogger(UserController.class);

    @PostMapping
    public User create(@Valid @RequestBody User user, BindingResult errors) {
        L.info("create user {}", user);
        if (errors.hasErrors()) {
            errors.getAllErrors().forEach(error -> {
                final FieldError fieldError = (FieldError) error;
                L.error("create user err: {} - {}", fieldError.getField(), error.getDefaultMessage());
            });
        }
        user.setId("1");
        return user;
    }

    @PutMapping("/{id:\\d+}")
    public User update(@Valid @RequestBody User user, BindingResult errors) {
        L.info("update user {}", user);
        if (errors.hasErrors()) {
            errors.getAllErrors().forEach(error -> L.error("create user err: {}", error.getDefaultMessage()));
        }
        user.setId("1");
        return user;
    }

    @GetMapping
    @JsonView(User.UserSimpleView.class)
    public List<User> query(UserQryCondition condition, @PageableDefault(page = 1, size = 10, sort = "username,asc") Pageable pageable) {
        final List<User> res = new ArrayList<>();
        res.add(new User());
        res.add(new User());
        res.add(new User());
        L.info("UserQryCondition {}", ReflectionToStringBuilder.toString(condition, ToStringStyle.MULTI_LINE_STYLE));
        L.info("pageable {}", pageable);
        return res;
    }

    @GetMapping("/{id:\\d+}")
    @JsonView(User.UserDetailView.class)
    public User getUserInfo(@PathVariable Long id, @PathVariable(name = "id") Long idddd) {
        L.info("getUserInfo qry id is {}", id);
        final User res = new User();
        res.setUsername("tom");
        return res;
    }
}
