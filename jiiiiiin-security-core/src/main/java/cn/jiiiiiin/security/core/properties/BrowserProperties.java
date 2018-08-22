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

    public String getLoginPage() {
        return loginPage;
    }

    public void setLoginPage(String loginPage) {
        this.loginPage = loginPage;
    }
}
