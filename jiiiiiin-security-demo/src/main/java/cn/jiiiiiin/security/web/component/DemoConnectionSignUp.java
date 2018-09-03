/**
 *
 */
package cn.jiiiiiin.security.web.component;

import cn.jiiiiiin.security.dto.User;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zhailiang
 * @see org.springframework.social.connect.ConnectionSignUp
 * @see cn.jiiiiiin.security.web.controller.UserController#register(User, HttpServletRequest)
 */
//@Component
public class DemoConnectionSignUp implements ConnectionSignUp {

    /* (non-Javadoc)
     * @see org.springframework.social.connect.ConnectionSignUp#execute(org.springframework.social.connect.Connection)
     */
    @Override
    public String execute(Connection<?> connection) {
        //TODO 根据社交用户信息（connection）默认创建用户并返回用户唯一标识

        // 下面这个代码是方便测试用
        return connection.getDisplayName();
    }

}
