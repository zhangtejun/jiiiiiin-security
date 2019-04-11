/**
 *
 */
package cn.jiiiiiin.security.core.social.support;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author zhailiang
 */
@Setter
@Getter
@ToString
public class SocialUserInfo {

    /**
     * 第三方授权服务提供商标识
     */
    private String providerId;
    /**
     * 第三方授权服务用户唯一标识，`openid`
     */
    private String providerUserId;
    /**
     * 第三方授权服务用户昵称
     */
    private String nickname;
    /**
     * 第三方授权服务用户头像
     */
    private String headimg;


}
