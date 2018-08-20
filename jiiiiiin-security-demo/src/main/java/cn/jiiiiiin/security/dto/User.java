package cn.jiiiiiin.security.dto;

import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;

/**
 * dto包下的内容都是用来封装RESTful输入输出数据的
 */
public class User {

    public interface UserSimpleView {
    }

    /**
     * 继承 {@link User.UserSimpleView} 就能把上层接口定义的东西继承下来
     */
    public interface UserDetailView extends UserSimpleView {
    }

    private String id;
    private String username;
    @NotBlank
    private String password;
    /**
     * 关于日期类型，最好的处理方式是请求和响应都传递时间戳，展示格式由前端自行控制
     */
    private Date birthday;

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

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", birthday=" + birthday +
                '}';
    }
}
