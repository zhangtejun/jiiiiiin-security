package cn.jiiiiiin.security.core.validate.code.entity;

import java.time.LocalDateTime;

/**
 * @author jiiiiiin
 */
public class ValidateCode {

    /**
     * 验证码
     */
    private String code;

    /**
     * 到期时间
     */
    private LocalDateTime expireTime;

    public ValidateCode(String code, LocalDateTime expireTime) {
        this.code = code;
        this.expireTime = expireTime;
    }

    /**
     * @param code
     * @param expireIn 多少秒后过期
     */
    public ValidateCode(String code, int expireIn) {
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireIn);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
