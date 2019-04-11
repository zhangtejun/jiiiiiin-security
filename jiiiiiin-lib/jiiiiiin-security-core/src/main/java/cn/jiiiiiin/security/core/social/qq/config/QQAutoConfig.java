/**
 *
 */
package cn.jiiiiiin.security.core.social.qq.config;

import cn.jiiiiiin.security.core.properties.SecurityProperties;
import cn.jiiiiiin.security.core.social.qq.connet.QQConnectionFactory;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactory;

/**
 * qq oauth2 授权配置类
 * <p>
 * 只有应用配置了qq相关的授权信息才启动当前配置
 * 升级到2.0：`https://www.jianshu.com/p/e6de152a0b4e`
 * @author zhailiang
 * @author jiiiiiin
 */
@Configuration
@ConditionalOnProperty(prefix = "jiiiiiin.security.social.qq", name = "app-id")
public class QQAutoConfig extends SocialConfigurerAdapter {

    @Autowired
    private SecurityProperties securityProperties;

    @Override
    public void addConnectionFactories(ConnectionFactoryConfigurer configurer, Environment environment) {
        configurer.addConnectionFactory(this.createConnectionFactory());
    }

    // 升级到springboot2之后，关联social修改了该接口
    //@Override
    protected ConnectionFactory<?> createConnectionFactory() {
        val qqConfig = securityProperties.getSocial().getQq();
        return new QQConnectionFactory(qqConfig.getProviderId(), qqConfig.getAppId(), qqConfig.getAppSecret());
    }

}
