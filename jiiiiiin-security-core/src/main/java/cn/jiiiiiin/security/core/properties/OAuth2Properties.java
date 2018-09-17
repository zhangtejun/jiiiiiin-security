/**
 *
 */
package cn.jiiiiiin.security.core.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.oauth2.config.annotation.builders.ClientDetailsServiceBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * 替代：
 * #security:
 * #  oauth2:
 * #    client:
 * #      client-id: immoc
 * #      client-secret: immocsecret
 * 配置
 *
 * @see ClientDetailsServiceBuilder.ClientBuilder 关于应用运行的第三方授权应用信息的配置
 *
 * @author zhailiang
 */
@Setter
@Getter
public class OAuth2Properties {

    /**
     * 使用jwt时为token签名的秘钥
     */
    private String jwtSigningKey = "jiiiiiin";
    /**
     * 客户端配置
     */
    private List<OAuth2ClientProperties> clients = new ArrayList<>();

}
