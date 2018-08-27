package cn.jiiiiiin.security.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @ConfigurationProperties(prefix = "jiiiiiin.security") 将会读取配置文件中对应prefix的配置项
 */
@ConfigurationProperties(prefix = "jiiiiiin.security")
public class SecurityProperties {

    private BrowserProperties browser = new BrowserProperties();

    private ValidateCodeProperties validate = new ValidateCodeProperties();

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
}
