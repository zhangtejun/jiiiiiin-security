package cn.jiiiiiin.security.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @ConfigurationProperties(prefix = "jiiiiiin.security") 将会读取配置文件中对应prefix的配置项
 */
@ConfigurationProperties(prefix = "jiiiiiin.security")
public class SecurityProperties {
    /**
     * 浏览器环境配置
     */
    private BrowserProperties browser = new BrowserProperties();
    /**
     * 验证码配置
     */
    private ValidateCodeProperties validate = new ValidateCodeProperties();

    /**
     * 社交登录配置
     */
    private SocialProperties social = new SocialProperties();

    public BrowserProperties getBrowser() {
        return browser;
    }

    public void setBrowser(BrowserProperties browser) {
        this.browser = browser;
    }

    public ValidateCodeProperties getValidate() {
        return validate;
    }

    public void setValidate(ValidateCodeProperties validate) {
        this.validate = validate;
    }

    public SocialProperties getSocial() {
        return social;
    }

    public void setSocial(SocialProperties social) {
        this.social = social;
    }
}
