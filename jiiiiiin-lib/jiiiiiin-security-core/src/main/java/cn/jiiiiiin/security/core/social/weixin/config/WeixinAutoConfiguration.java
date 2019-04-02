/**
 *
 */
package cn.jiiiiiin.security.core.social.weixin.config;

import cn.jiiiiiin.security.core.properties.SecurityProperties;
import cn.jiiiiiin.security.core.properties.WeixinProperties;
import cn.jiiiiiin.security.core.social.view.CustomBindingConnectView;
import cn.jiiiiiin.security.core.social.weixin.connect.WeixinConnectionFactory;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 微信登录配置
 *
 * @author zhailiang
 */
@Configuration
@ConditionalOnProperty(prefix = "jiiiiiin.security.social.weixin", name = "app-id")
@AllArgsConstructor
public class WeixinAutoConfiguration extends SocialConfigurerAdapter {

    private final SecurityProperties securityProperties;

    @Override
    public void addConnectionFactories(ConnectionFactoryConfigurer configurer, Environment environment) {
        configurer.addConnectionFactory(this.createConnectionFactory());
    }

    // @Override
    protected ConnectionFactory<?> createConnectionFactory() {
        val weixinConfig = securityProperties.getSocial().getWeixin();
        return new WeixinConnectionFactory(weixinConfig.getProviderId(), weixinConfig.getAppId(),
                weixinConfig.getAppSecret(), securityProperties.getBrowser().getProxyUri());
    }

    /**
     * 声明一个绑定微信授权信息的响应视图
     * "connect/weixinConnect" 是解绑成功的响应视图声明
     * "connect/weixinConnected" 是绑定成功的响应视图声明
     *
     * @return
     * @see CustomBindingConnectView#renderMergedOutputModel(Map, HttpServletRequest, HttpServletResponse)
     */
    @Bean({"connect/weixinConnect", "connect/weixinConnected"})
    @ConditionalOnMissingBean(name = "weixinConnectedView")
    public View weixinConnectedView() {
        return new CustomBindingConnectView();
    }

}
