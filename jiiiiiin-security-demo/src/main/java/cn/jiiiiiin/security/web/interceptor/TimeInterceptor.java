package cn.jiiiiiin.security.web.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 如果在拦截器中需要直接获取到请求接口对应传递的参数（方法参数），其实也需要手动去request中取
 * 所以如果要想直接拿到参数，可以使用aop切片拦截
 *
 * @author jiiiiiin
 */
@Component
public class TimeInterceptor implements HandlerInterceptor {

    final static Logger L = LoggerFactory.getLogger(TimeInterceptor.class);
    private static final String START_TIME = "START_TIME";

    /**
     * 相较于filter，interceptor就多了第三个参数，方便我们得到请求需要的接口定义信息
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @param o
     * @return 如果返回false流程将会中断
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        if (o instanceof HandlerMethod) {
            L.debug("time interceptor preHandle {}", o);
            final HandlerMethod handlerMethod = (HandlerMethod) o;
            L.debug("handlerMethod {}", handlerMethod);
            httpServletRequest.setAttribute(START_TIME, System.currentTimeMillis());
        }
        return true;
    }

    /**
     * 如果执行的控制器抛出异常，当前钩子不会被调用
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @param o
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        L.debug("time interceptor postHandle");
    }

    /**
     * 当前钩子始终会被调用
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @param o
     * @param e                   如果执行请求链条抛出了异常，则该参数不为空
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        if (o instanceof HandlerMethod) {
            L.debug("time interceptor afterCompletion");
            final long startTime = (long) httpServletRequest.getAttribute(START_TIME);
            L.debug("time interceptor: {}", (System.currentTimeMillis() - startTime));
            // 如果定义了@ExceptionHandler(UserNotExistException.class)这样的异常处理器，处理了请求流程中抛出的异常，则这里就的e就为null
            if (e != null) {
                L.error("time interceptor err: {}", e);
            }
        }
    }
}
