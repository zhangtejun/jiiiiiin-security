/**
 *
 */
package cn.jiiiiiin.security.app.server;

import cn.jiiiiiin.security.core.properties.SecurityProperties;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * 认证服务器配置
 * <p>
 * <p>
 * ![](https://ws4.sinaimg.cn/large/0069RVTdgy1fuqnerfq5uj30w10g3q4o.jpg)
 * <p>
 * 上图也是下面的授权模式中，使用最多的授权码模式的交互流程；
 * <p>
 * ![](https://ws1.sinaimg.cn/large/0069RVTdgy1fuqn6o8vm2j30ox0bm3z0.jpg)
 *
 * <p>
 * https://oauth.net/2/
 * <p>
 * 4.1.  Authorization Code Grant
 * <p>
 * The authorization code grant type is used to obtain both access
 * tokens and refresh tokens and is optimized for confidential clients.
 * Since this is a redirection-based flow, the client must be capable of
 * interacting with the resource owner's user-agent (typically a web
 * browser) and capable of receiving incoming requests (via redirection)
 * from the authorization server.
 * <p>
 * +----------+
 * | Resource |
 * |   Owner  |
 * |          |
 * +----------+
 * ^
 * |
 * (B)
 * +----|-----+          Client Identifier      +---------------+
 * |         -+----(A)-- & Redirection URI ---->|               |
 * |  User-   |                                 | Authorization |
 * |  Agent  -+----(B)-- User authenticates --->|     Server    |
 * |          |                                 |               |
 * |         -+----(C)-- Authorization Code ---<|               |
 * +-|----|---+                                 +---------------+
 * |    |                                         ^      v
 * (A)  (C)                                        |      |
 * |    |                                         |      |
 * ^    v                                         |      |
 * +---------+                                      |      |
 * |         |>---(D)-- Authorization Code ---------'      |
 * |  Client |          & Redirection URI                  |
 * |         |                                             |
 * |         |<---(E)----- Access Token -------------------'
 * +---------+       (w/ Optional Refresh Token)
 * <p>
 * Note: The lines illustrating steps (A), (B), and (C) are broken into
 * two parts as they pass through the user-agent.
 * <p>
 * Figure 3: Authorization Code Flow
 *
 * <p>
 * 添加了`@EnableAuthorizationServer`注解之后，项目就可以当做一个授权服务提供商，给第三方应用提供oauth授权服务
 * <p>
 * `/oauth/authorize::GET`接口来提供oauth流程第一步，提供第三方服务获取“授权码”
 * <p>
 * 所需参数：
 * response_type
 * REQUIRED.  Value MUST be set to "code".
 * <p>
 * client_id
 * REQUIRED.  The client identifier as described in Section 2.2.
 * <p>
 * redirect_uri
 * OPTIONAL.  As described in Section 3.1.2.
 * <p>
 * <p>
 * `/oauth/authorize::GET`接口来提供oauth流程第一步，提供第三方服务获取“授权码”
 * <p>
 * `/oauth/token::POST`接口来提供oauth流程第而步，第三方服务通过“授权码”来获取授权令牌
 *
 * @author zhailiang
 */
@Configuration
@EnableAuthorizationServer
public class CustomAuthorizationServerConfig {
//public class CustomAuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

//    @Autowired
//    private UserDetailsService userDetailsService;
//
//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    @Autowired
//    private TokenStore tokenStore;
//
//    @Autowired(required = false)
//    private JwtAccessTokenConverter jwtAccessTokenConverter;
//
//    @Autowired(required = false)
//    private TokenEnhancer jwtTokenEnhancer;
//
//    @Autowired
//    private SecurityProperties securityProperties;
//
//    /**
//     * 认证及token配置
//     */
//    @Override
//    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//        endpoints.tokenStore(tokenStore)
//                .authenticationManager(authenticationManager)
//                .userDetailsService(userDetailsService);
//
//        if (jwtAccessTokenConverter != null && jwtTokenEnhancer != null) {
//            TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
//            List<TokenEnhancer> enhancers = new ArrayList<>();
//            enhancers.add(jwtTokenEnhancer);
//            enhancers.add(jwtAccessTokenConverter);
//            enhancerChain.setTokenEnhancers(enhancers);
//            endpoints.tokenEnhancer(enhancerChain).accessTokenConverter(jwtAccessTokenConverter);
//        }
//
//    }
//
//    /**
//     * tokenKey的访问权限表达式配置
//     */
//    @Override
//    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
//        security.tokenKeyAccess("permitAll()");
//    }
//
//    /**
//     * 客户端配置
//     */
//    @Override
//    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//        InMemoryClientDetailsServiceBuilder builder = clients.inMemory();
//        if (ArrayUtils.isNotEmpty(securityProperties.getOauth2().getClients())) {
//            for (OAuth2ClientProperties client : securityProperties.getOauth2().getClients()) {
//                builder.withClient(client.getClientId())
//                        .secret(client.getClientSecret())
//                        .authorizedGrantTypes("refresh_token", "authorization_code", "password")
//                        .accessTokenValiditySeconds(client.getAccessTokenValidateSeconds())
//                        .refreshTokenValiditySeconds(2592000)
//                        .scopes("all");
//            }
//        }
//    }

}
