package cn.jiiiiiin.security.core.properties;

/**
 * 图形验证码配置类
 * ![](https://ws1.sinaimg.cn/large/0069RVTdgy1fuo9h1z6mrj30t30eu0tl.jpg)
 *
 * @author jiiiiiin
 */
public class ImageCodeProperties extends SmsCodeProperties {

    public ImageCodeProperties() {
        super();
        // 默认4位图形验证码长度
        setLength("4");
    }

    /**
     * 图形验证码宽度
     */
    private String width = "100";
    /**
     * 图形验证码高度
     */
    private String height = "40";
    /**
     * 验证码文本字符大小  默认为30
     */
    private String size = "30";

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

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
