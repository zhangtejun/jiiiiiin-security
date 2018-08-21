package cn.jiiiiiin.security.web.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;

/**
 * 使用filter的局限：
 * 1、（简单方法）拿不到请求访问哪一个控制器
 *
 * @author jiiiiiin
 */
// spring boot会自动应用注册到容器中的bean filter
//@Component
public class TimeFilter implements Filter {

    final static Logger L = LoggerFactory.getLogger(TimeFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        L.info("time filter init");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        L.info("time filter start");
        final long start = System.currentTimeMillis();
        filterChain.doFilter(servletRequest, servletResponse);
        L.info("time filter: {}", (System.currentTimeMillis() - start));
        L.info("time filter finish");
    }

    @Override
    public void destroy() {
        L.info("time filter destroy");
    }
}
