package cn.jiiiiiin.hystrix;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * <dependency>
 * <groupId>org.springframework.cloud</groupId>
 * <artifactId>spring-cloud-netflix-core</artifactId>
 * <version>1.3.6.RELEASE</version>
 * </dependency>
 * <p>
 * http://www.itmuch.com/spring-cloud/edgware-new-zuul-fallback/
 * 对于Dalston及更低版本，要想为Zuul提供回退，zuul 熔断配置
 *
 * @author jiiiiiin
 */
public class TestZuulFallbackProvider {
//public class TestZuulFallbackProvider implements ZuulFallbackProvider {
//
//    @Override
//    public String getRoute() {
//        // 表明是为哪个微服务提供回退，*表示为所有微服务提供回退
////        return "eureka-client";
//        return "*";
//    }
//
//    @Override
//    public ClientHttpResponse fallbackResponse() {
//        return new ClientHttpResponse() {
//            @Override
//            public HttpStatus getStatusCode() throws IOException {
//                // fallback时的状态码
//                return HttpStatus.OK;
//            }
//
//            @Override
//            public int getRawStatusCode() throws IOException {
//                // 数字类型的状态码，本例返回的其实就是200，详见HttpStatus
//                return this.getStatusCode().value();
//            }
//
//            @Override
//            public String getStatusText() throws IOException {
//                // 状态文本，本例返回的其实就是OK，详见HttpStatus
//                return this.getStatusCode().getReasonPhrase();
//            }
//
//            @Override
//            public void close() {
//
//            }
//
//            @Override
//            public InputStream getBody() throws IOException {
//                // 响应体
//                return new ByteArrayInputStream("用户微服务不可用，请稍后再试。".getBytes());
//            }
//
//            @Override
//            public HttpHeaders getHeaders() {
//                // headers设定
//                HttpHeaders headers = new HttpHeaders();
//                headers.setContentType(MediaType.APPLICATION_JSON);
//                return headers;
//            }
//        };
//    }
}
