package cn.jiiiiiin.security.core.validate.code;

import org.apache.commons.lang3.StringUtils;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;

/**
 * @author jiiiiiin
 */
public class ImageCode {

    /**
     * 验证码
     */
    private String code;
    /**
     * 根据验证码生成的图片
     */
    private BufferedImage image;

    /**
     * 到期时间
     */
    private LocalDateTime expireTime;

    public ImageCode(String code, BufferedImage image, LocalDateTime expireTime) {
        this.code = code;
        this.image = image;
        this.expireTime = expireTime;
    }

    /**
     * @param code
     * @param image
     * @param expireIn 多少秒后过期
     */
    public ImageCode(String code, BufferedImage image, int expireIn) {
        this.code = code;
        this.image = image;
        this.expireTime = LocalDateTime.now().plusSeconds(expireIn);
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public LocalDateTime getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(LocalDateTime expireTime) {
        this.expireTime = expireTime;
    }

    /**
     * 判断验证码是否过期
     *
     * @return 返回true标明已经过期
     */
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expireTime);
    }
}
