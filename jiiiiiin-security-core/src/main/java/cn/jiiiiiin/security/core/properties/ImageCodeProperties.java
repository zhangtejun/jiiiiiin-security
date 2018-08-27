package cn.jiiiiiin.security.core.properties;

import cn.jiiiiiin.security.core.validate.code.ValidateCodeController;

import java.util.HashSet;
import java.util.Set;

/**
 * 图形验证码配置类
 * ![](https://ws1.sinaimg.cn/large/0069RVTdgy1fuo9h1z6mrj30t30eu0tl.jpg)
 *
 * @author jiiiiiin
 */
public class ImageCodeProperties {

    /**
     * 图形验证码宽度
     */
    private String width = "100";
    /**
     * 图形验证码高度
     */
    private String height = "40";
    /**
     * 验证码生成位数
     */
    private String length = "4";
    /**
     * 验证码文本字符大小  默认为30
     */
    private String size = "30";
    /**
     * 验证码有效期
     * 请求时候会检测是否在请求参数中携带 {@link ValidateCodeController#KEY_EXPIRE_IN}
     * 如果携带则覆盖配置项
     */
    private int expireIn = 60;

    /**
     * 需要进行拦截的接口
     *
     * 默认会添加 {@link cn.jiiiiiin.security.core.validate.code.ValidateCodeFilter#LOGIN_PROCESSING_URL} 身份认证接口
     */
    private Set<String> interceptorUrls = new HashSet<>();

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public int getExpireIn() {
        return expireIn;
    }

    public void setExpireIn(int expireIn) {
        this.expireIn = expireIn;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Set<String> getInterceptorUrls() {
        return interceptorUrls;
    }

    public void setInterceptorUrls(Set<String> interceptorUrls) {
        this.interceptorUrls = interceptorUrls;
    }
}
