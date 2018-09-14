/**
 *
 */
package cn.jiiiiiin.security.web.component;

import cn.jiiiiiin.security.dto.User;
import org.springframework.security.core.Authentication;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 提供给{@link org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository#findUserIdsWithConnection}使用，用来对授权用户进行“静默注册”的一个组件
 * <p>
 * 注册完毕之后，social将会把业务和授权用户记录到`UserConnection`中，并直接由{@link org.springframework.social.security.SocialAuthenticationProvider#authenticate(Authentication)}完成登录逻辑，而不需要再路由到授权注册页面进行注册、登录
 *
 * @author zhailiang
 * @author jiiiiiin
 * @see org.springframework.social.connect.ConnectionSignUp
 * @see cn.jiiiiiin.security.web.controller.UserController#register(User, HttpServletRequest, HttpServletResponse)
 */
//@Component
public class DemoConnectionSignUp implements ConnectionSignUp {

    /* (non-Javadoc)
     * @see org.springframework.social.connect.ConnectionSignUp#execute(org.springframework.social.connect.Connection)
     */
    @Override
    public String execute(Connection<?> connection) {
        //TODO 根据社交用户信息（connection）默认创建用户（注册到业务系统）并返回用户唯一标识

        // 下面这个代码是方便测试用
        return connection.getDisplayName();
    }

}
