package cn.jiiiiiin.security.web.aspect;


import cn.jiiiiiin.security.web.interceptor.TimeInterceptor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 * 声明spring aop
 * 参考：
 * https://docs.spring.io/spring/docs/4.3.18.RELEASE/spring-framework-reference/htmlsingle/#aop
 *
 * 1. 通过@Aspect将一个bean声明为一个切片类
 * 2. 定义切入点（
 * 声明在上面时候起作用[@Before()/@After()/@AfterThrowing/@Around()] 分别是在被拦截的方法执行前后和抛出异常的时机被调用）、
 * 声明需要在那些方法上起作用
 *
 *  如：@Around("execution(* cn.jiiiiiin.security.web.controller.UserController.*(..))") 就是来声明切入点
 *
 * 3. 增强（起作用时候执行的业务逻辑）
 *  即切入点下面的的方法体
 *
 * @author jiiiiiin
 */
@Aspect
@Component
public class TimeAspect {

    final static Logger L = LoggerFactory.getLogger(TimeAspect.class);

    /**
     * 拦截
     * @see cn.jiiiiiin.security.web.controller.UserController 中的所有方法
     *
     * @param pjp 这个对象中包含了被拦截的对应执行方法的原信息
     */
    @Around("execution(* cn.jiiiiiin.security.web.controller.UserController.*(..))")
    public Object handlerControllerMethod(ProceedingJoinPoint pjp) throws Throwable {
        L.debug("time aspect start {}", pjp);
        final long start = System.currentTimeMillis();
        // 在切片里面可以拿到待调用方法的实际请求数据（方法参数）
        final Object[] args = pjp.getArgs();
        for (Object arg: args) {
            L.debug("time aspect param: {}", arg);
        }

        // 得到的就是执行的控制器的接口返回的数据
        final Object res = pjp.proceed();

        L.debug("time aspect record: {}", (System.currentTimeMillis() - start));
        L.debug("time aspect end");
        return res;
    }

}
