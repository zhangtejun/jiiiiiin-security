/**
 *
 */
package cn.jiiiiiin.security.core.properties;

import cn.jiiiiiin.security.core.properties.adapter.SocialProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 微信登录配置项
 *
 * @author zhailiang
 */
@Setter
@Getter
@NoArgsConstructor
public class WeixinProperties extends SocialProperties {

    /**
     * 第三方id，用来决定发起第三方登录的url，默认是 weixin。
     */
    private String providerId = "weixin";

}
