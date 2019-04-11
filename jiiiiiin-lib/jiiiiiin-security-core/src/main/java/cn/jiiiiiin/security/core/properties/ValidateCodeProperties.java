package cn.jiiiiiin.security.core.properties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 框架验证码配置类
 *
 * @author jiiiiiin
 */
@Setter
@Getter
@NoArgsConstructor
@ToString
public class ValidateCodeProperties {

    private ImageCodeProperties imageCode = new ImageCodeProperties();
    private SmsCodeProperties smsCode = new SmsCodeProperties();

}
