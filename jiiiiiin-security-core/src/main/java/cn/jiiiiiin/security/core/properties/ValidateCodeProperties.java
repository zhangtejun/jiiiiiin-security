package cn.jiiiiiin.security.core.properties;

/**
 * 框架验证码配置类
 *
 * @author jiiiiiin
 */
public class ValidateCodeProperties {

    private ImageCodeProperties imageCode = new ImageCodeProperties();

    public ImageCodeProperties getImageCode() {
        return imageCode;
    }

    public void setImageCode(ImageCodeProperties imageCode) {
        this.imageCode = imageCode;
    }
}
