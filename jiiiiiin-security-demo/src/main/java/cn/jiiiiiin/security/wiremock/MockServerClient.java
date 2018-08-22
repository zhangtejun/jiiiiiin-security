package cn.jiiiiiin.security.wiremock;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
//import static org.apache.http.client.methods.*;

/**
 * wire mock客户端
 * 参考：
 * http://wiremock.org/docs/getting-started/
 *
 * @author jiiiiiin
 */
public class MockServerClient {

    /**
     * wiremock server的端口
     */
    private static final int PORT = 9000;

    public static void main(String[] args) throws IOException {
        configureFor(PORT);
        removeAllMappings();
        mock("/order/1", "01");

    }

    /**
     *
     * @param url mock的url
     * @param fileName mock数据文件名
     * @throws IOException
     */
    private static void mock(String url, String fileName) throws IOException {
        final ClassPathResource resource = new ClassPathResource("mock/response/" + fileName + ".json");
        final String data = FileUtils.readFileToString(resource.getFile(), "UTF-8");

        stubFor(get(urlPathEqualTo(url))
                .willReturn(aResponse()
                        .withBody(data)
                        .withStatus(200)));
    }
}
