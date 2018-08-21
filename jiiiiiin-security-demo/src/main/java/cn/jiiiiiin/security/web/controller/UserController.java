package cn.jiiiiiin.security.web.controller;

import cn.jiiiiiin.security.dto.User;
import cn.jiiiiiin.security.dto.UserQryCondition;
import cn.jiiiiiin.security.exception.UserNotExistException;
import com.fasterxml.jackson.annotation.JsonView;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jiiiiiin
 * // @RestController 表明需要将当前类声明为一个RESTful的api提供者
 * // @RequestMapping 作用是将http请求的url映射到java的方法上去
 * // @RequestParam 表示需要将http请求参数映射成java方法的参数
 * // public List<User> query(@RequestParam String username) 标识请求需要携带一个参数（GET）
 * // @RequestParam(name="username") String name 标识传递的参数的key和方法的参数不统一的时候，通过name或者value来进行映射
 * // @PathVariable 作用是将http请求的url参数映射到java的方法对应参数上去
 * //@GetMapping("/user/{id}")
 * //public User getUserInfo(@PathVariable Long id, @PathVariable(name = "id") Long idddd) {
 * // 使用正则表达式对 path variable进行校验@GetMapping("/user/{id:\\d+}")
 * <p>
 * // @RequestBody 映射请求体到java方法参数
 * // public User create(@RequestBody User user) 需要加上注解之后，才能解析前端传上来的json对象中的对应字段的值
 * // spring mvc直接将前端传递的参数映射到一个实体类：public List<User> query(UserQryCondition condition){
 * <p>
 * // @Valid 注解和BindingResult验证请求参数的合法性并处理校验结果
 * // public User create(@Valid @RequestBody User user) 如果直接这样写，那么前端如果没有按要求传递，则会直接不进入请求方法体，直接响应400 标识请求的格式错误，因为后台会校验对应字段的格式，但是校验失败
 * // 如果需要在校验失败也进入方法体，需要添加BindingResult到方法参数中
 * // BindingResult需要配置@Valid注解使用，在校验失败时候，会将错误消息（校验失败消息）映射到该对象
 * <p>
 * // @PageableDefault 用来指定分页参数的默认值
 * // @PageableDefault(page = 1, size = 10, sort = "username,asc") Pageable pageable
 * // 如果前端没有传递PageableDefault设置的默认值，那么就使用默认值映射到Pageable参数对应字段中
 * <p>
 * // @JsonView 控制接口的json输出内容
 * // 使用接口来声明多个视图（比如查询用户列表、和查询单用户对象）
 * // 如
 * //public interface UserSimpleView {
 * //}
 * //
 * 继承 {@link User.UserSimpleView} 就能把上层接口定义的东西继承下来
 * <p>
 * //public interface UserDetailView extends User.UserSimpleView {
 * //}
 * // 在值对象的get方法上指定视图
 * // 如：
 * //@JsonView(User.UserSimpleView.class)
 * //public String getUsername() {
 * //        return username;
 * //        }
 * //
 * //@JsonView(User.UserDetailView.class)
 * //public String getPassword() {
 * //        return password;
 * //        }
 * <p>
 * // 在Controller方法上指定视图
 * // @JsonView(User.UserSimpleView.class)
 */

@RestController
@RequestMapping("/user")
public class UserController {

    final static Logger L = LoggerFactory.getLogger(UserController.class);

    final static String AVATAR_SAVE_PATH = "/Users/jiiiiiin/Documents/IdeaProjects/jiiiiiin-security/jiiiiiin-security-demo/src/main/resources/static";

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

    @DeleteMapping("/{id:\\d+}")
    public void delete(@PathVariable String id) {
        L.info("delete id {}", id);
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
    public User getUserInfo(@PathVariable String id, @PathVariable(name = "id") Long idddd) {
        L.info("getUserInfo qry id is {}", id);
        // 测试异常处理
//        throw new UserNotExistException(id);
        final User res = new User();
        res.setUsername("tom");
        return res;
    }

    /**
     * 文件上传接口示例：
     * 注意文件上传
     * MultipartFile file 这个file的命名很特殊，需要和测试用例的第一个参数一致
     *
     * @param file
     */
    @PostMapping("/upload/avatar")
    @JsonView(User.UserSimpleView.class)
    public User uploadAvatar(MultipartFile file) throws IOException {
        // file.getOriginalFilename() 原始文件名
        L.info("uploadAvatar: {} {} {}", file.getName(), file.getOriginalFilename(), file.getSize());

        final File localFile = new File(AVATAR_SAVE_PATH, System.currentTimeMillis() + ".jpg");
        // 将传上来的文件写到本地
        file.transferTo(localFile);
        // 或者拿到上传文件的输入流写到其他地方
        // file.getInputStream()

        final User res = new User();
        res.setUsername("tom");
        res.setAvatar("http://www.baidu.com/avatar.jpg");
        return res;
    }

    /**
     * 文件下载接口示例
     *
     * @param id
     * @param request
     * @param response
     */
    @GetMapping("/{id:\\d+}/download/avatar")
    public void downloadAvatar(@PathVariable String id, HttpServletRequest request, HttpServletResponse response) {
        // jdk7 之后直接将io操作放在try后面的括号中，jvm会自动帮我们把流释放（关闭）
        try (
                final InputStream inputStream = new FileInputStream(new File(AVATAR_SAVE_PATH, "1534835212463.jpg"));
                final OutputStream outputStream = response.getOutputStream();
        ) {
            response.setContentType("application/x-download");
            // 指定下载的时候服务器定义被下载文件的名称:tom_avatar.jpg
            response.addHeader("Content-Disposition", "attachment;filename=tom_avatar.jpg");
            // 将本地文件写到响应体中
            IOUtils.copy(inputStream, outputStream);
            outputStream.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
