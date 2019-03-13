package cn.jiiiiiin.hystrix;

import com.netflix.hystrix.exception.HystrixTimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * http://www.itmuch.com/spring-cloud/edgware-new-zuul-fallback/
 * http://blog.didispace.com/spring-cloud-zuul-fallback-improve/
 * 通过源码我们可以了解，Zuul将你自定义的fallbackprovider保存在一个Set集合中，并作为HttpClientRibbonCommandFactory构造器的参数。
 * 当zuul在转发请求时最终会利用AbstractRibbonCommand进行处理。通过源码我们知道AbstractRibbonCommand继承了HystrixCommand，所以真正转发请求的业务逻辑是在重写HystrixCommand类的run方法中进行的。
 * 我们知道HystrixCommand提供getFallback()方法，这个方法的作用是当run()方法执行出现异常时，会自动调用getFallback()方法，从而完成降级功能。(HystrixCommand是Hystrix的知识，有兴趣的同学可以参照官方git文档)。
 * <p>
 * https://www.jianshu.com/p/f5cdce29890d
 * <p>
 * Edgware及更高版本
 *
 * @author jiiiiiin
 */
@Slf4j
public class TestNewZuulFallbackProvider implements FallbackProvider {
    @Override
    public String getRoute() {
        // 表明是为哪个微服务提供回退，*表示为所有微服务提供回退
        return "*";
    }

    /**
     * 该方法会让我定义一个ClientHttpResponse作为当异常出现时的返回内容。
     *
     * @param route
     * @param cause 也就是说此时是用能力获取异常信息的
     * @return
     */
    @Override
    public ClientHttpResponse fallbackResponse(String route, Throwable cause) {
        if (cause != null) {
            log.error("网关收到降级错误", cause);
        }
        if (cause instanceof HystrixTimeoutException) {
            return response(HttpStatus.GATEWAY_TIMEOUT);
        } else {
            return response(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ClientHttpResponse response(final HttpStatus status) {
        return new ClientHttpResponse() {
            @Override
            public HttpStatus getStatusCode() throws IOException {
                return status;
            }

            @Override
            public int getRawStatusCode() throws IOException {
                return status.value();
            }

            @Override
            public String getStatusText() throws IOException {
                return status.getReasonPhrase();
            }

            @Override
            public void close() {
            }

            @Override
            public InputStream getBody() throws IOException {
                return new ByteArrayInputStream("服务不可用，请稍后再试。".getBytes());
            }

            @Override
            public HttpHeaders getHeaders() {
                // headers设定
                HttpHeaders headers = new HttpHeaders();
                MediaType mt = new MediaType("application", "json", Charset.forName("UTF-8"));
                headers.setContentType(mt);
                return headers;
            }
        };
    }
}
