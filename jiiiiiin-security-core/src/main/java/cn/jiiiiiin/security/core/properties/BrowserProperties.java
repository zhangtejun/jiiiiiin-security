package cn.jiiiiiin.security.core.properties;

/**
 * @author jiiiiiin
 */
public class BrowserProperties {

    /**
     * 身份认证（登录）页面
     * 默认页面："/signIn.html"
     */
    private String loginPage = "/signIn.html";

    /**
     * '记住我'功能的有效时间，单位（秒），默认1小时
     */
    private int rememberMeSeconds = 3600;

    public String getLoginPage() {
        return loginPage;
    }

    public void setLoginPage(String loginPage) {
        this.loginPage = loginPage;
    }

    public int getRememberMeSeconds() {
        return rememberMeSeconds;
    }

    public void setRememberMeSeconds(int rememberMeSeconds) {
        this.rememberMeSeconds = rememberMeSeconds;
    }
}
