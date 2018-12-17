package cn.jiiiiiin.manager.controller;

import cn.jiiiiiin.module.common.exception.BusinessErrException;
import com.baomidou.mybatisplus.extension.api.R;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 处理项目中控制器抛出的异常
 *
 * @author jiiiiiin
 */
@ControllerAdvice
@Slf4j
public class ControllerExceptionHandler {

    // 自定义响应accept为非html的json响应数据：
    // @ExceptionHandler标识当前接口会处理那个异常
    // 即项目中的控制器，无论那个接口抛出对应的异常，spring都会回调下面的方法来进行异常的处理
    @ExceptionHandler(Exception.class)
    // 标识需要返回json格式数据
    @ResponseBody
    // 标识返回的状态码
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public R<String> handlerUserNotExistException(Exception e) {
        log.error("全局异常处理捕获到的错误 {}", e.getClass().getSimpleName());
        log.error("错误堆栈--》", e);
        if (e instanceof NullPointerException) {
            return R.failed("服务器内部错误【空指针异常】");
        } else if (e instanceof BusinessErrException) {
            val businessErrException = (BusinessErrException) e;
            val apiResult = new R<String>();
            apiResult.setCode(businessErrException.getCode());
            apiResult.setMsg(businessErrException.getMessage());
            return apiResult;
        } else {
            return R.failed(e.getMessage());
        }
    }
}
