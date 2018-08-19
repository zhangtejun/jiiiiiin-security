- [代码结构](https://ws2.sinaimg.cn/large/006tNbRwgy1fue02z4h20j31kw0rc0y8.jpg)


### 问题集合：

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