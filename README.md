### 参考：

> [Spring Security Tutorial 《Spring Security 教程》](https://waylau.gitbooks.io/spring-security-tutorial/content/)

> [社区 Spring Security 从入门到进阶系列教程](http://www.spring4all.com/article/428)

### 关键点

- ![代码结构](https://ws2.sinaimg.cn/large/006tNbRwgy1fue02z4h20j31kw0rc0y8.jpg)

- ![属性配置](https://ws1.sinaimg.cn/large/006tNbRwgy1fuilgv8orxj30rx0emgm6.jpg)

- spring social

  ![](https://ws2.sinaimg.cn/large/0069RVTdgy1fuqn54pzzmj30wd0ilaby.jpg)

  这里的第二步其实流程会像下面这样进行：

  关键点就是第三方应用引导用户到授权服务提供商去进行**授权页面**的显示和请求授权码，如果用户在服务提供商页面同意授权，那么服务提供商将下发一个**授权码**给第三方应用，
  之后第三方应用才能走上面的第三步流程：**申请令牌**，凭借获得的授权码，provider 验证授权码之后才会下发真正的授权令牌（token）；
  申请令牌的过程应该是在第三方应用的后台去请求 provider server；

  ![](https://ws4.sinaimg.cn/large/0069RVTdgy1fuqnerfq5uj30w10g3q4o.jpg)

  上图也是下面的授权模式中，使用最多的授权码模式的交互流程；

  ![](https://ws1.sinaimg.cn/large/0069RVTdgy1fuqn6o8vm2j30ox0bm3z0.jpg)

  授权码模式和密码模式、客户端模式不同的地方就在于，其他两种模式获取授权码是直接在第三方应用上完成的（第三方应用直接去请求授权码，而不是 provider“主动”下发）；

  授权码模式需要第三方应用有自己的服务器，去通过授权码获取 token，而没有 server 的应用一般使用简化模式

  授权码模式获得的 token 应该是存储于 server 端，安全性相对较高，简化模式，token 是直接返回给第三方应用的页面；

  以上是一个 Oauth 的授权流程；

  下面是 spring social 的授权登录流程：

  ![](https://ws2.sinaimg.cn/large/0069RVTdgy1fuqnpxoqo6j30wg0hr0v0.jpg)

  spring social 的作用就是帮助我们封装了这套流程到 ss 过滤器链的`SocialAuthenticationFilter`中；

  ![](https://ws4.sinaimg.cn/large/0069RVTdgy1fuqoup6qazj31kw0jqn0l.jpg)

  主要涉及的类：

  ![](https://ws2.sinaimg.cn/large/0069RVTdgy1fuqp901tqxj30y20izwgv.jpg)

  作为第三方比如接 qq 的实现步骤：

  1.先实现`Api`接口，因为每一家服务供应商提供的用户信息数据结构都是不一样的，就需要根据不同供应商去实现该接口
  参考：cn.jiiiiiin.security.core.social.qq.api.QQImpl 2.构建出 service provider（一般需要两个组件，OAuth2Operations 这个可以使用默认实现，如果供应商实现的 OAuth 比较标准、Api 就使用上面第一步得到的组件）
  参考:`cn.jiiiiiin.security.core.social.qq.connet.QQServiceProvider` 3.构建一个`Connection Factory`(一般需要两个自定义组件，就是上面两步得到的）
  参考：`cn.jiiiiiin.security.core.social.qq.connet.QQConnectionFactory` 4.得到`Connection`，就可以拿到用户信息
  通过`Connection Factory`构建 5.得到用户信息之后通过`UserConnectionRepository`就可以构建出 ss 框架自定义的`UserConnection`表中的数据，将 service provider

  ![](https://ws4.sinaimg.cn/large/0069RVTdgy1fuxbmqywxfj31kw0vntfu.jpg)

  > https://coding.imooc.com/lesson/134.html#mid=6890

  OAuth2AuthenticationService 主要是用来进行 auth 流程的执行【`OAuth2AuthenticationService#getAuthToken`方法】，其会调用上面的 connection--> oauth2template 来执行 auth 流程；

  ```java
  public SocialAuthenticationToken getAuthToken(HttpServletRequest request, HttpServletResponse response) throws SocialAuthenticationRedirectException {
      // code就是授权服务提供商在成功授权之后给的授权码[Authorization Code]
      // 参考：http://wiki.connect.qq.com/%E4%BD%BF%E7%94%A8authorization_code%E8%8E%B7%E5%8F%96access_token
      // 判断是否有授权码，即auth流程第三步返回
  			String code = request.getParameter("code");
  			if (!StringUtils.hasText(code)) {
                  // 认为是流程的第一步
  				OAuth2Parameters params =  new OAuth2Parameters();
  				params.setRedirectUri(buildReturnToUrl(request));
  				setScope(request, params);
  				params.add("state", generateState(connectionFactory, request));
  				addCustomParameters(params);
                  // 如果是第一步就会组织参数，抛出异常，让social将请求重定向到qq的授权地址
  				throw new SocialAuthenticationRedirectException(getConnectionFactory().getOAuthOperations().buildAuthenticateUrl(params));
  			} else if (StringUtils.hasText(code)) {
  				try {
                      // 如果有授权码
  					String returnToUrl = buildReturnToUrl(request);
                      // 那授权码去换取令牌，第4、5步
                      // 这里很可能出现异常，因为第三方返回的响应数据，social解析不出来的时候
                      // 发送获取授权码的默认实现：extractAccessGrant(getRestTemplate().postForObject(accessTokenUrl, parameters, Map.class));
                      // 标明框架期望得到的是一个json数据【RestTemplate】
                      // 如果返回的数据内容类型非`application/json`或者数据不是json格式，就会出问题，qq返回的就不是这样的数据结构
                      // access_token=FE04************************CCE2&expires_in=7776000&refresh_token=88E4************************BE14
                      //http://wiki.connect.qq.com/%E4%BD%BF%E7%94%A8authorization_code%E8%8E%B7%E5%8F%96access_token
                      // 解决方式就是自己实现template，添加相应的数据格式解析器，参考：cn.jiiiiiin.security.core.social.qq.connet.QQOAuth2Template#createRestTemplate
                      // restTemplate.getMessageConverters().add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
  					AccessGrant accessGrant = getConnectionFactory().getOAuthOperations().exchangeForAccess(code, returnToUrl, null);
  					// TODO avoid API call if possible (auth using token would be fine)
  					Connection<S> connection = getConnectionFactory().createConnection(accessGrant);
  					return new SocialAuthenticationToken(connection, null);
  				} catch (RestClientException e) {
  					logger.debug("failed to exchange for access", e);
  					return null;
  				}
  			} else {
  				return null;
  			}
  		}
  ```

  授权完成之后  服务提供商的  用户信息会封装到`Conncetion`中；

  之后再进入 ss 身份认证的流程，`SocialAuthenticationToken`会包含着 Connection 一起丢给`AuthenticationManager`进行身份认证；

  `SocialAuthenticationProvider`会调用`JdbcUsersConnectionRepository`来  通过 connection 中的授权用户信息去数据库[UserConnection 表]中查询一个`UserId`，之后调用`SocialUserDetailsService`去查询正在的业务系统的用户信息`SocialUserDetails`，重新构建 Authtication Token 标记为认证成功， 之后防止到 ss 的 context 中， 最后防止到 session 中，标识授权登录完成；

  - 绑定解绑 spring social 服务，`ConnectController`:

    1. `/connect::GET`提供查询当前登录用户查询第三方授权绑定信息集合的接口；

       需要自定义响应视图，参考`CustomConnectionStatusView`

    2. `/connect/[provideId]::POST`提供绑定当前登录用户到对第三方授权服务提供商的接口；

       需要自定义响应视图，参考`CustomConnectionStatusView`

* 常见问题：

![](https://ws1.sinaimg.cn/large/0069RVTdgy1fuso8tlo2fj311w0hc74n.jpg)
上面的错误是因为没有在或者配置错误，qq 的开放平台配置的第三方应用的回调域，即流程的第三部，在服务提供商页面授权完毕之后，其会回调我们配置的这个域名；

而实际在第一步导向到 qq 服务器的时候，spring social 已经为我们处理了`redirec_url`这个参数：
`https://graph.qq.com/oauth2.0/show?which=error&display=pc&error=100010&client_id=100550231&response_type=code&redirect_uri=http%3A%2F%2Flocalhost%3A8080%2Fauth%2Fqq&state=c1a93ae2-8da1-4a1a-bc78-5ec5bc8b6b5d`

开发中这个域名还是本地 localhost 就和配置在开放平台的不匹配就报错了；

`redirect_uri=http%3A%2F%2Flocalhost%3A8080%2Fauth%2Fqq`这个是我们的登录授权页面发起 qq 渠道授权的时候 href 的接口，即我们自身的接口；

解决方式，让这里的 redirect_uri 和平台注册时候填写的回调域保持同一个域名；

可以使用修改本机的 host 文件完成；
![](https://ws4.sinaimg.cn/large/0069RVTdgy1fuswrpang1j30qq0k8jrt.jpg)

或者部署到测试服务器，进行正式的联调；

- spring security 相关：

  > https://spring.io/projects/spring-security > https://docs.spring.io/spring-security/site/docs/4.2.7.RELEASE/reference/htmlsingle/

  - 配置规划

  ![](https://ws2.sinaimg.cn/large/0069RVTdgy1fuqepk8hyhj30zg0h3jtv.jpg)

  - 验证码

  ![](https://ws1.sinaimg.cn/large/0069RVTdgy1fupfn8ecd6j30yv0fiwfo.jpg)

  - 记住我

  ![](https://ws1.sinaimg.cn/large/0069RVTdgy1fuoes59unqj30zo0fuabd.jpg)

  - [session 管理](https://coding.imooc.com/lesson/134.html#mid=6992)

    - session 超时处理
      配置：

      ```properties
      server:
        port: 8080
        session:
          # 设置session过期时间，单位是秒，默认30分钟，但是在springboot中（嵌入式）,最少的时间是1分钟，可以查看TomcatEmbeddedServletContainerFactory#configureSession方法`sessionTimeout = Math.max(TimeUnit.SECONDS.toMinutes(sessionTimeout), 1L);`
          timeout: 10
      ```

      如何自定义 session 失效导致的需要授权登录提示？

    - session 并发控制
    - session 集群管理

  - 核心功能：
    ![](https://ws2.sinaimg.cn/large/006tNbRwgy1fuia8dpyrej30dv0bvdg4.jpg)
    ![](https://ws2.sinaimg.cn/large/0069RVTdgy1fuoesrwcz1j31kw0jvgp1.jpg)
    参考：https://juejin.im/post/5a434de6f265da43333eae7d

    ```java
       o.s.s.web.DefaultSecurityFilterChain     : Creating filter chain: OrRequestMatcher [requestMatchers=[Ant [pattern='/css/**'], Ant [pattern='/js/**'], Ant [pattern='/images/**'], Ant [pattern='/webjars/**'], Ant [pattern='/**/favicon.ico'], Ant [pattern='/error']]], []
              2018-08-24 10:46:59.538  INFO 5296 --- [           main] o.s.s.web.DefaultSecurityFilterChain     : Creating filter chain: org.springframework.security.web.util.matcher.AnyRequestMatcher@1, [org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter@64e247e, org.springframework.security.web.context.SecurityContextPersistenceFilter@1be3a294, org.springframework.security.web.header.HeaderWriterFilter@7d49fe37, org.springframework.security.web.authentication.logout.LogoutFilter@73633230, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter@356ab368, org.springframework.security.web.savedrequest.RequestCacheAwareFilter@729d1428, org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter@4425b6ed, org.springframework.security.web.authentication.AnonymousAuthenticationFilter@66df362c, org.springframework.security.web.session.SessionManagementFilter@231c521e, org.springframework.security.web.access.ExceptionTranslationFilter@5af1b221, org.springframework.security.web.access.intercept.FilterSecurityInterceptor@5c30decf]
              2018-08-24 10:46:59.558  INFO 5296 --- [           main] o.s.s.web.DefaultSecurityFilterChain     : Creating filter chain: org.springframework.boot.actuate.autoconfigure.ManagementWebSecurityAutoConfiguration$LazyEndpointPathRequestMatcher@7ca8f5d7, [org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter@2dc319cf, org.springframework.security.web.context.SecurityContextPersistenceFilter@4f5df012, org.springframework.security.web.header.HeaderWriterFilter@3ad9fea, org.springframework.web.filter.CorsFilter@1690929, org.springframework.security.web.authentication.logout.LogoutFilter@53b9952f, org.springframework.security.web.authentication.www.BasicAuthenticationFilter@46df794e, org.springframework.security.web.savedrequest.RequestCacheAwareFilter@38f3dbbf, org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter@574e4184, org.springframework.security.web.authentication.AnonymousAuthenticationFilter@34d26a68, org.springframework.security.web.session.SessionManagementFilter@556e4588, org.springframework.security.web.access.ExceptionTranslationFilter@574413bd, org.springframework.security.web.access.intercept.FilterSecurityInterceptor@7da9b32c]
              2018-08-24 10:46:59.561  INFO 5296 --- [           main] o.s.s.web.DefaultSecurityFilterChain     : Creating filter chain: OrRequestMatcher [requestMatchers=[Ant [pattern='/**']]], [org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter@7945986a, org.springframework.security.web.context.SecurityContextPersistenceFilter@4144d4a, org.springframework.security.web.header.HeaderWriterFilter@30a1b2ad, org.springframework.security.web.authentication.logout.LogoutFilter@37b44e8e, org.springframework.security.web.authentication.www.BasicAuthenticationFilter@11ec2b2f, org.springframework.security.web.savedrequest.RequestCacheAwareFilter@20276412, org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter@1dae9e61, org.springframework.security.web.authentication.AnonymousAuthenticationFilter@442151d1, org.springframework.security.web.session.SessionManagementFilter@436d2bb9, org.springframework.security.web.access.ExceptionTranslationFilter@20608ef4, org.springframework.security.web.access.intercept.FilterSecurityInterceptor@7c31e01f]
              2018-08-24 10:47:00.175  INFO 5296 --- [           m
    ```

  * 浏览器相关 security 配置参考: bean::BrowserSecurityConfig、MyUserDetailsService

    - 个性化用户认证流程

      ![](https://ws1.sinaimg.cn/large/006tNbRwgy1fujs30pbkcj31kw10atc6.jpg)

      上图就是框架认证流程的核心处理流程

      如果需要自定义:
      ![]()

      https://coding.imooc.com/lesson/134.html#mid=6872

      - 自定义登录页面

        ![](https://ws3.sinaimg.cn/large/006tNbRwgy1fuiiuwx7wxj31kw0s5wjo.jpg)
        为了区分渠道，需要像上图那样去重新定义 security 框架的处理逻辑；

        关键在于 spring security 的授权配置：

        ```java
          protected void configure(HttpSecurity http) throws Exception {
                  http
                         //...
                          .and()
                          // formLogin() 指定身份认证的方式
                          // 下面这样配置就改变了默认的httpBasic认证方式，而提供一个登录页面
                          .formLogin()
                          // 自定义登录页面
          //                .signInUrl("/signIn.html")
                          .signInUrl("/signIn")
                          // 自定义登录交易请求接口，会被UsernamePasswordAuthenticationFilter所识别作为requiresAuthenticationRequestMatcher
                          .loginProcessingUrl("/signIn")
                          .and()
            }
        ```

        获取请求的用户名和密码关键在`UsernamePasswordAuthenticationFilter#attemptAuthentication` 解析用户名密码哪里，目前默认是获取 request 中的 req params，也就是表单参数，如果要支持 json 请求，需要继承该类，手动实现，可以参考：
        https://docs.spring.io/spring-security/site/docs/4.2.7.RELEASE/reference/htmlsingle/#form-login-filter

      - 自定义登录成功处理流程
        如果前端使用 ajax 异步请求一个需要授权登录的交易，那么默认的 spring security 的处理方式是在登录完成之后会重定向到上一个交易，如果要干预或者说，登录成功之后需要返回给前端登录用户的消息，那么就必须要进行这里的自定义；
        关键在于`AuthenticationSuccessHandler`
        ```java
        .and()
        // formLogin() 指定身份认证的方式
        // 下面这样配置就改变了默认的httpBasic认证方式，而提供一个登录页面
        //...
        // 配置自定义认证成功处理器
        .successHandler(jAuthenticationSuccessHandler)
        ```
      - 自定义登录失败处理流程

- ![RESTFul API](https://ws3.sinaimg.cn/large/006tNbRwgy1fufeoc5gxdj31kw0yswnl.jpg)

- 自定义配置
  参考:
  ![](https://ws1.sinaimg.cn/large/0069RVTdgy1fuo9h1z6mrj30t30eu0tl.jpg)

* [kaptcha 集成](https://www.jianshu.com/p/1f2f7c47e812)

* 使用 Swagger 自动生成文档

  根据代码自动生成文档，提供给前端开发人员识别接口；

  [集成](https://mvnrepository.com/artifact/io.springfox/springfox-swagger2)

  ![](https://ws1.sinaimg.cn/large/006tNbRwgy1fui7xqrglgj31kw0ocab6.jpg)

  ```java
      @SpringBootApplication
      @RestController
      @EnableSwagger2
      public class App {}

    // 常用注解：@ApiParam、@ApiOperation

      @GetMapping("/{id:\\d+}")
      @JsonView(User.UserDetailView.class)
      @ApiOperation(value = "用户查询服务")
      public User getUserInfo(@ApiParam(value = "用户id") @PathVariable String id, @PathVariable(name = "id") Long idddd) {

    // 常用注解：@ApiModelProperty
    public class UserQryCondition {

        private String username;
        @ApiModelProperty(value = "年龄起始值")
        private int age;
        // 年龄区间
        @ApiModelProperty(value = "年龄终止值")
  ```

  - 自定义

    `UserDetailsService`用来通过用户名获取`UserDetails`用户标识对象；

- 异步处理 REST 服务

  - 使用 Callable 来处理

  ![](<(https://ws4.sinaimg.cn/large/006tNbRwgy1fuhcqkylckj31kw0rm0v2.jpg)>)

  如果使用异步方式处理请求，那么请求就不占用容器的【主线程】的资源数，使得容器可以处理更多请求而不被阻塞，提升服务器的吞吐量；
  对于浏览器来说，还是一个【正常】的请求，因为耗时还是 1.x 秒得到响应；

  特点：
  子线程必须是在主线程中开启

  - 有一种情况：

  ![](https://ws1.sinaimg.cn/large/006tNbRwgy1fuhdliloxxj30x40ezmxz.jpg)

  即接收请求的服务器是一个前置服务器，真正进行耗时操作的是外围一台服务器， 应用 1 只负责接收消息，之后将消息放到消息队列，而真正处理业务是在应用 2 中去监控消息队列，取出并处理，处理完毕之后将结果返给消息队列，应用 1 在实时的去取结果；
  而真正的请求的响应式在应用 1 从队列中取出处理结果之后返回给前端的；

  针对这种场景，上面的处理方式就不能满足需求，需要使用 DeferredResult 来进行处理，参考`@RequestMapping("/async/order2")处理逻辑

  还需要注意：

  ```java
      /**
       * 针对异步接口的拦截器配置需要通过下面的接口进行注册（相应的拦截器也需要重写）
       * 否则常规的拦截器是拦截不到异步的接口
       *
       * @param configurer
       */
      @Override
      public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
          super.configureAsyncSupport(configurer);
  //        configurer.registerDeferredResultInterceptors()
  //        configurer.registerCallableInterceptors()
      }
  ```

* springboot aop、拦截器、过滤器相关:

  ![springboot aop、拦截器、过滤器相关](https://ws3.sinaimg.cn/large/006tNbRwgy1fuh5paluivj30n90glgm0.jpg)

  `@ControllerAdvice`就是如控制器异常处理类上面声明的注解；

  如果控制器中抛出的异常，在外围还是抛出的话，那就会进行层层传递；

  如果 filter 还没有处理异常，就会抛到容器（如：tomcat）最终显示到前端；

- springboot 默认错误处理控制器 `org.springframework.boot.autoconfigure.web.BasicErrorController`

  ```java
  // /error是默认错误视图的路径
  @Controller
  @RequestMapping("${server.error.path:${error.path:/error}}")
  public class BasicErrorController extends AbstractErrorController {

      // 表示请求参数头中的Accept被认定为html的时候进入该接口进行错误处理
      @RequestMapping(produces = "text/html")

      // 否则进入：
      @RequestMapping
      @ResponseBody
      public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
  ```

  手动处理 404 网页版：需要在`/jiiiiiin-security/jiiiiiin-security-demo/src/main/resources/resources/error/404.html`创建对应状态码的页面；
  这样对应状态码的网页版的错误页面就可以被订制；

### 问题集合：

- spring security 常见错误：

  - signInUrl 配置：

  ```java
    .signInUrl(LOGIN_URL)
  ```

  这里的配置如果需要使用控制器来做渠道控制（渲染登录页面或者返回 json 提示），那么接口的名称不要和被渲染的页面名称一致，比如接口【signIn】而页面名称【signIn.html】这样会导致视图解析器报错

  - 默认开启了 csrf，但是没有做合理配置，登录的时候就报：
    ```xml
    There was an unexpected error (type=Forbidden, status=403).
    Could not verify the provided CSRF token because your session was not found.
    ```
    调试期间可以先关闭这个特性：
    ```java
      .and()
            .csrf().disable();
    ```

**需要先创建对应数据库，启动 redis 服务**

- [redis 安装](https://www.youtube.com/watch?v=JGvbEk4jtrU)

- `Cannot determine embedded database driver class for database type NONE`：

  因为在`jiiiiiin-security-core`中声明了 jdbc 依赖，但是没有配置数据库链接配置则会报错：

  ```xml
          <dependency>
              <groupId>org.springframework.boot</groupId>
              <artifactId>spring-boot-starter-jdbc</artifactId>
          </dependency>
  ```

  解决，配置 jdbc 链接配置信息：

  ```properties
  spring:
    datasource:
      driver-class-name: com.mysql.jdbc.Driver
  #    http://database.51cto.com/art/201005/199278.htm
      url: jdbc:mysql://127.0.0.1:3306/jiiiiiin-security-demo?&useUnicode=true&characterEncoding=utf8&autoReconnect=true&failOverReadOnly=false
      username: root
      password: root
  ```

- `Caused by: java.lang.IllegalArgumentException: No Spring Session store is configured: set the 'spring.session.store-type' property`
  因为`jiiiiiin-security-browser`中声明了 spring session 的依赖，但是没有配置相关依赖配置故报错：
  解决先关闭依赖配置：
  ```properties
  spring:
    session:
      store-type: none
  ```
- 项目启动访问需要“登录信息”，因为 spring security 默认开启认证，可以先关闭默认配置：

  ```properties
  security:
    basic:
      enabled: false
  ```

- 怎么将 demo 项目打出可执行 jar：

  解决：

  ```xml
       <plugins>
          <!--将项目打包成一个可执行的jar https://docs.spring.io/spring-boot/docs/1.5.15.RELEASE/reference/htmlsingle/#getting-started-first-application-executable-jar-->
          <plugin>
              <groupId>org.springframework.boot</groupId>
              <artifactId>spring-boot-maven-plugin</artifactId>
              <executions>
                  <execution>
                      <goals>
                          <goal>repackage</goal>
                      </goals>
                  </execution>
              </executions>
          </plugin>

      </plugins>
  ```

- `java.lang.AssertionError: No value at JSON path "$.length()": java.lang.IllegalArgumentException: json can not be null or empty`

  因为测试的方法没有返回正确的 json 数据；
