- [代码结构](https://ws2.sinaimg.cn/large/006tNbRwgy1fue02z4h20j31kw0rc0y8.jpg)
- [RESTFul API](https://ws3.sinaimg.cn/large/006tNbRwgy1fufeoc5gxdj31kw0yswnl.jpg)

### 关键点
    + springboot 默认错误处理控制器 `org.springframework.boot.autoconfigure.web.BasicErrorController`
    
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
        
        手动处理404网页版：需要在`/jiiiiiin-security/jiiiiiin-security-demo/src/main/resources/resources/error/404.html`创建对应状态码的页面；
        这样对应状态码的网页版的错误页面就可以被订制；
            

### 问题集合：

    **需要先创建对应数据库，启动redis服务**
    
    + [redis安装](https://www.youtube.com/watch?v=JGvbEk4jtrU)

    + `Cannot determine embedded database driver class for database type NONE`：
    
        因为在`jiiiiiin-security-core`中声明了jdbc依赖，但是没有配置数据库链接配置则会报错：
        ```xml
                <dependency>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-starter-jdbc</artifactId>
                </dependency>
        ```
        解决，配置jdbc链接配置信息：
        ```properties
        spring:
          datasource:
            driver-class-name: com.mysql.jdbc.Driver
        #    http://database.51cto.com/art/201005/199278.htm
            url: jdbc:mysql://127.0.0.1:3306/jiiiiiin-security-demo?&useUnicode=true&characterEncoding=utf8&autoReconnect=true&failOverReadOnly=false
            username: root
            password: root

        ```
        
    + `Caused by: java.lang.IllegalArgumentException: No Spring Session store is configured: set the 'spring.session.store-type' property`
        
        因为`jiiiiiin-security-browser`中声明了spring session的依赖，但是没有配置相关依赖配置故报错：
        解决先关闭依赖配置：
        ```properties
        spring:
          session:
            store-type: none
        ```
        
    + 项目启动访问需要“登录信息”，因为spring security默认开启认证，可以先关闭默认配置：
    
        ```properties
        security:
          basic:
            enabled: false
        ```
    
    + 怎么将demo项目打出可执行jar：
        
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
        
    + `java.lang.AssertionError: No value at JSON path "$.length()": java.lang.IllegalArgumentException: json can not be null or empty`
    
        因为测试的方法没有返回正确的json数据；
        
        
    