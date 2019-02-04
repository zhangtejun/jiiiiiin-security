/**
 *
 */
package cn.jiiiiiin.security.core.properties;

import cn.jiiiiiin.security.core.properties.adapter.SocialProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * QQ登录配置项
 * <p>
 * 继承{@link CustomSocialProperties}之后就可以使用框架提供的两个配置来设置应用id和密码对应qq服务授权提供商的
 * <p>
 * 提供给初始化{@link cn.jiiiiiin.security.core.social.qq.connet.QQConnectionFactory}使用
 *
 * @author zhailiang
 * @author jiiiiiin
 */
@Setter
@Getter
@NoArgsConstructor
public class QQProperties extends SocialProperties {

    /**
     * 服务提供商标识
     * <p>
     * 第三方id，用来决定发起第三方登录的url，默认是 qq。
     *
     * social provider提供者通过该标识来找到具体由那个服务提供商来进行处理auth流程:
     * @see org.springframework.social.security.SocialAuthenticationFilter
     */
    private String providerId = "qq";

}
