package cn.jiiiiiin.security.dto;

import com.fasterxml.jackson.annotation.JsonView;

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

    private String username;
    private String password;

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
}
