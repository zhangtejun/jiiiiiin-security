/**
 *
 */
package cn.jiiiiiin.security.core.social.qq.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.oauth2.TokenStrategy;

/**
 * {@link org.springframework.social.ApiBinding}的实现，用于获取qq的用户信息
 * <p>
 * 需要注意该类的实例不能是单例，因为 {@link AbstractOAuth2ApiBinding#accessToken} 字段是一个状态字段
 * <p>
 * 参考：
 * <p>
 * https://coding.imooc.com/lesson/134.html#mid=6888
 * <p>
 * 下面是spring social的授权登录流程：
 * <p>
 * ![](https://ws2.sinaimg.cn/large/0069RVTdgy1fuqnpxoqo6j30wg0hr0v0.jpg)
 *
 * @author zhailiang
 * @author jiiiiiin
 * @see AbstractOAuth2ApiBinding#accessToken 字段就是进行oauth授权得到的token令牌
 * <p>
 * 每个用户获取的令牌都不一样，每个进行oauth授权流程的用户都会有当前一个对象实例
 * @see AbstractOAuth2ApiBinding#restTemplate 用来帮助我们发送请求到服务提供商，获取用户信息
 * <p>
 * 获取qq用户信息，qq的接口文档：
 * http://wiki.connect.qq.com/api%E5%88%97%E8%A1%A8
 * <p>
 * http://wiki.connect.qq.com/get_user_info
 */
@Slf4j
public class QQImpl extends AbstractOAuth2ApiBinding implements QQ {

    /**
     * 拉取openid qq接口：http://wiki.connect.qq.com/openapi%E8%B0%83%E7%94%A8%E8%AF%B4%E6%98%8E_oauth2-0
     */
    private static final String URL_GET_OPENID = "https://graph.qq.com/oauth2.0/me?access_token=%s";

    /**
     * 拉取用户信息qq接口
     */
    private static final String URL_GET_USERINFO = "https://graph.qq.com/user/get_user_info?oauth_consumer_key=%s&openid=%s";

    // === 定义拉取qq用户信息接口`get_user_info`所需的未定义参数：appId、openId
    private String appId;

    private String openId;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * 构造实例
     * <p>
     * 获取openid
     * <p>
     * 设置access_token参数策略
     *
     * @param accessToken oauth令牌
     * @param appId       qq授权app id
     */
    public QQImpl(String accessToken, String appId) {
        // 修改ACCESS_TOKEN参数设置策略，这里根据qq要求放在请求参数中，框架默认实现是放在请求头
        // Indicates that the access token should be carried as a query parameter named "access_token".
        super(accessToken, TokenStrategy.ACCESS_TOKEN_PARAMETER);

        this.appId = appId;

        final String url = String.format(URL_GET_OPENID, accessToken);
        final String openIdInfo = getRestTemplate().getForObject(url, String.class);

        // 返回数据格式参考：http://wiki.connect.qq.com/%E8%8E%B7%E5%8F%96%E7%94%A8%E6%88%B7openid_oauth2-0
        // {"client_id":"YOUR_APPID","openid":"YOUR_OPENID"}
        log.debug("获取openid结果 {}", openIdInfo);

        // 截取openid
        this.openId = StringUtils.substringBetween(openIdInfo, "\"openid\":\"", "\"}");
    }

    /**
     * 获取用户信息
     *
     * @see cn.jiiiiiin.security.core.social.qq.api.QQ#getUserInfo()
     */
    @Override
    public QQUserInfo getUserInfo() {

        // 基类会帮我们拼接`access_token`参数根据初始化实例时候传递的token strategy
        String url = String.format(URL_GET_USERINFO, appId, openId);
        String result = getRestTemplate().getForObject(url, String.class);

        log.debug("获取qq用户信息结果 {}", result);

        try {
            final QQUserInfo userInfo = OBJECT_MAPPER.readValue(result, QQUserInfo.class);
            userInfo.setOpenId(openId);
            return userInfo;
        } catch (Exception e) {
            throw new RuntimeException("获取qq用户信息失败", e);
        }
    }

}
