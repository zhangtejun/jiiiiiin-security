package cn.jiiiiiin.validation.spring;

import cn.jiiiiiin.validation.core.Style;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @author jiiiiiin
 */
@Slf4j
public class MethodArgumentCheckArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        log.info("supportsParameter:: {}", methodParameter.hasParameterAnnotation(Style.class));
        return methodParameter.hasParameterAnnotation(Style.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        val style = methodParameter.getParameterAnnotation(Style.class);
        val type = style.value();
        val vo = style.vo();
        log.info("nativeWebRequest:: {} {}", type, vo);
        log.info("nativeWebRequest:: {}", nativeWebRequest);
        switch (type) {
            case ENTITY:
                val temp = vo.newInstance();
                val res = ValidationUtils.validateEntity(temp);
                log.info("check res:: {}", res);
                break;
            default:
        }
        return null;
    }
}
