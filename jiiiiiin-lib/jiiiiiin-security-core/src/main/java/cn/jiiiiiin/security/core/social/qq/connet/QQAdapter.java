/**
 *
 */
package cn.jiiiiiin.security.core.social.qq.connet;

import cn.jiiiiiin.security.core.social.qq.api.QQ;
import cn.jiiiiiin.security.core.social.qq.api.QQUserInfo;
import lombok.val;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;

/**
 * {@link org.springframework.social.connect.ConnectionFactory}中的成员变量，用于将第七步获取的个性化用户信息，适配成标准的Connection实例；
 * <p>
 * 用来将服务提供商的个性化数据和spring social默认统一格式数据模型进行适配
 * <p>
 * 泛型参数标明需要适配的服务提供商数据类型{@link org.springframework.social.ApiBinding}
 *
 * @author zhailiang
 * @author jiiiiiin
 */
public class QQAdapter implements ApiAdapter<QQ> {

    /**
     * 用来对服务提供商的服务进行心跳测试
     *
     * @param api
     * @return true标识对应服务提供商的服务是畅通的
     */
    @Override
    public boolean test(QQ api) {
        // TODO 可以发请求对服务提供商接口进行探测
        return true;
    }

    /**
     * 数据适配，将个性化数据信息设置到标准结构
     * 需要在此设置 {@link Connection}所需要的数据项设置到values参数中，以便工厂创建连接对接之后交给{@link org.springframework.social.connect.UsersConnectionRepository}进行数据持久化
     *
     * @param api
     * @param values 包含了创建 {@link Connection}所需要的一些数据项
     */
    @Override
    public void setConnectionValues(QQ api, ConnectionValues values) {
        val userInfo = api.getUserInfo();
        // 显示的用户名
        values.setDisplayName(userInfo.getNickname());
        // 头像
        values.setImageUrl(userInfo.getFigureurl_qq_1());
        // 个人主页url地址，qq没有
        values.setProfileUrl(null);
        // 服务商的用户标识id，即用户唯一标识
        values.setProviderUserId(userInfo.getOpenId());
    }

    @Override
    public UserProfile fetchUserProfile(QQ api) {
        // TODO 待
        return null;
    }

    @Override
    public void updateStatus(QQ api, String message) {
        // 用来想个人主页发一条最新消息，某些应用才可以玩，qq没有
        //do noting
    }

}
