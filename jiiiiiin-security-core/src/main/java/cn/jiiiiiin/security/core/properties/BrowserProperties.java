package cn.jiiiiiin.security.core.properties;

/**
 * @author jiiiiiin
 */
public class BrowserProperties {

    /**
     * 身份认证（登录）页面
     * 默认页面："/signIn.html"
     */
    private String signInUrl = "/signIn.html";

    /**
     * 社交登录，如果需要用户注册，跳转的页面
     */
    private String signUpUrl = "/imooc-signUp.html";

    /**
     * '记住我'功能的有效时间，单位（秒），默认1小时
     */
    private int rememberMeSeconds = 3600;

    public String getSignInUrl() {
        return signInUrl;
    }

    public void setSignInUrl(String signInUrl) {
        this.signInUrl = signInUrl;
    }

    public int getRememberMeSeconds() {
        return rememberMeSeconds;
    }

    public void setRememberMeSeconds(int rememberMeSeconds) {
        this.rememberMeSeconds = rememberMeSeconds;
    }

    public String getSignUpUrl() {
        return signUpUrl;
    }

    public void setSignUpUrl(String signUpUrl) {
        this.signUpUrl = signUpUrl;
    }
}
