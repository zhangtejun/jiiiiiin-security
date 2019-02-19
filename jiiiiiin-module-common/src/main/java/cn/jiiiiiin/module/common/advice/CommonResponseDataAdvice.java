package cn.jiiiiiin.module.common.advice;

import cn.jiiiiiin.module.common.annotation.IgnoreResponseAdvice;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.enums.ApiErrorCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * `@RestControllerAdvice`标识对响应进行统一拦截处理
 * @author jiiiiiin
 */
@RestControllerAdvice
@Slf4j
public class CommonResponseDataAdvice implements ResponseBodyAdvice<Object> {

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * @param methodParameter
     * @param aClass
     * @return
     */
    @Override
    public boolean supports(MethodParameter methodParameter,
                            Class<? extends HttpMessageConverter<?>> aClass) {

        if (methodParameter.getDeclaringClass().isAnnotationPresent(IgnoreResponseAdvice.class)) {
            return false;
        }

        if (methodParameter.getMethod().isAnnotationPresent(IgnoreResponseAdvice.class)) {
            return false;
        }

        return true;
    }

    @Override
    public Object beforeBodyWrite(Object o,
                                  MethodParameter methodParameter,
                                  MediaType mediaType,
                                  Class<? extends HttpMessageConverter<?>> aClass,
                                  ServerHttpRequest serverHttpRequest,
                                  ServerHttpResponse serverHttpResponse) {

        R<Object> response = new R<>();
        response.setCode(ApiErrorCode.SUCCESS.getCode());
        // 解决：`https://my.oschina.net/u/1757225/blog/1543715`
        // TODO 目前版本的SpringBoot不要在控制器返回String
//        if (o instanceof String) {
//            try {
//                return objectMapper.writeValueAsString(o);
//            } catch (JsonProcessingException e) {
//                log.error("转换服务器端响应数据出错", e);
//            }
//        }
        if (o instanceof R) {
            response = (R<Object>) o;
        } else if (null == o) {
            return response;
        } else {
            response.setData(o);
        }
        log.debug("服务器端响应数据 {}", response);
        return response;
    }
}
