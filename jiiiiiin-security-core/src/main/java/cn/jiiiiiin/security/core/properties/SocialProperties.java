/**
 *
 */
package cn.jiiiiiin.security.core.properties;

import org.springframework.social.security.SocialAuthenticationFilter;

/**
 * 社交登录配置项
 *
 * @author zhailiang
 */
public class SocialProperties {

    /**
     * 社交登录功能拦截的url
     *
     * @see SocialAuthenticationFilter#DEFAULT_FILTER_PROCESSES_URL
     */
    private String filterProcessesUrl = "/auth";

    private QQProperties qq = new QQProperties();

	private WeixinProperties weixin = new WeixinProperties();

    public QQProperties getQq() {
        return qq;
    }

    public void setQq(QQProperties qq) {
        this.qq = qq;
    }

    public String getFilterProcessesUrl() {
        return filterProcessesUrl;
    }

    public void setFilterProcessesUrl(String filterProcessesUrl) {
        this.filterProcessesUrl = filterProcessesUrl;
    }

	public WeixinProperties getWeixin() {
        return weixin;
    }

    public void setWeixin(WeixinProperties weixin) {
        this.weixin = weixin;
    }

}
