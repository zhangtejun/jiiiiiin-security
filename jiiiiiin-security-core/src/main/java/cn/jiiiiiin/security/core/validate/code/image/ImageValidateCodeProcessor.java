/**
 *
 */
package cn.jiiiiiin.security.core.validate.code.image;

import cn.jiiiiiin.security.core.validate.code.impl.AbstractValidateCodeProcessor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

import javax.imageio.ImageIO;

/**
 * 图片验证码处理器
 *
 * @author zhailiang
 */
@Component
public class ImageValidateCodeProcessor extends AbstractValidateCodeProcessor<ImageCode> {

    /**
     * 发送图形验证码，将其写到响应中
     */
    @Override
    protected void send(ServletWebRequest request, ImageCode imageCode) throws Exception {
        if (imageCode.getCaptcha() != null) {
            // 输出图片流
            imageCode.getCaptcha().out(request.getResponse().getOutputStream());
        } else if (imageCode.getImage() != null) {
            ImageIO.write(imageCode.getImage(), "JPEG", request.getResponse().getOutputStream());
        }
    }

}
