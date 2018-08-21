package cn.jiiiiiin.security.dto;

import cn.jiiiiiin.security.validator.MyConstraint;
import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Past;
import java.util.Date;

/**
 * dto包下的内容都是用来封装RESTful输入输出数据的
 * <p>
 * 校验参考：
 * Hibernate Validator
 *
 * @author jiiiiiin
 */
public class User {

    public interface UserSimpleView {
    }

    /**
     * 继承 {@link UserSimpleView} 就能把上层接口定义的东西继承下来
     */
    public interface UserDetailView extends UserSimpleView {
    }

    private String id;
    @MyConstraint(message = "测试校验注解")
    private String username;
    @NotBlank(message = "密码不能为空")
    private String password;
    /**
     * 关于日期类型，最好的处理方式是请求和响应都传递时间戳，展示格式由前端自行控制
     */
    @Past
    private Date birthday;
    private String avatar;

    @JsonView(UserSimpleView.class)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @JsonView(UserDetailView.class)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @JsonView(UserSimpleView.class)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @JsonView(UserSimpleView.class)
    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @JsonView(UserSimpleView.class)
    public String getAvatar() {
        return avatar;
    }

}
