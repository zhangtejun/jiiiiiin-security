# jiiiiiin-security

一个前后端分离的内管基础项目

# 原则

+ 以最少的表结构字段完成一个基础应用，以便在以此完成实际项目时有更多的扩充自由

# 计划
| 功能 | 完成状态 | 简介 |
| ------ | ------ | ------ |
| 代码自动生成 | 100% | [服务端3层代码自动生成](https://github.com/Jiiiiiin/jiiiiiin-security/blob/master/jiiiiiin-module-common/src/main/java/cn/jiiiiiin/module/common/generator/CodeGenerator.java) |
| RBAC后端权限控制 | 95% | [基于Spring Security的后端RBAC权限控制](https://github.com/Jiiiiiin/jiiiiiin-security/tree/master/jiiiiiin-security-authorize) |
| RBAC前端权限控制 | 70% | [1.基于vue-viewplus，实现了一个自定义模块](http://jiiiiiin.cn/vue-viewplus/#/global_api?id=mixin-) <br> [2.实现前端页面可访问性控制，通过路由拦截，判断用户待访问页面是否在授权资源集合中](https://github.com/Jiiiiiin/jiiiiiin-security/blob/master/jiiiiiin-client-manager/src/plugin/vue-viewplus/rbac.js#L250) <br> [3.实现可见页面的局部UI组件的**可使用性或可见性**控制，基于自定义`access`指令，对比声明的接口urls（资源别名）是否在授权接口或资源集合中](https://github.com/Jiiiiiin/jiiiiiin-security/blob/master/jiiiiiin-client-manager/src/plugin/vue-viewplus/rbac.js#L124)|
| 全面集成vue-viewplus | 70% | [vue-viewplus一个简化Vue应用开发的工具库](https://github.com/Jiiiiiin/vue-viewplus) |
| 会话并发控制 | 95% | [使用SpringSecurity#concurrency-control实现应用中同一用户在同时只能有一个是终端（渠道）成功登录应用，后登录终端会导致前一个会话失效](https://github.com/Jiiiiiin/jiiiiiin-security/blob/master/jiiiiiin-security-browser/src/main/java/cn/jiiiiiin/security/browser/config/BrowserSpringSecurityBaseConfig.java#L123) |
| 会话集群共享 | 95% | [使用Spring Session与Redis实现会话的共享存储和集群部署](https://github.com/Jiiiiiin/jiiiiiin-security/blob/master/jiiiiiin-server-manager/src/main/resources/application.yml#L20) |
| d2-mng-page | 100% | [自定义管理页面组件（统一管理：分页、检索、table、编辑）,为了统一审美](https://github.com/Jiiiiiin/jiiiiiin-security/blob/master/jiiiiiin-client-manager/src/components/d2-mng-page/index.vue) |
| 角色管理 | 95% | 用来管理系统定义的角色 |
| 资源管理 | 95% | 用来管理系统定义的资源 |
| 用户管理 | 95% | 用来管理系统存在的用户 |
| 接口管理 | 95% | 用来管理后台对应的接口集合 |


# 功能截图

![image-20181106162706851](https://ws3.sinaimg.cn/large/006tNbRwgy1fwyf81a19lj31kw0w0awb.jpg)

![image-20181207163547451](https://ws4.sinaimg.cn/large/006tNbRwgy1fxy9om3ct3j31hc0u0dpu.jpg)

用户管理页面截图

![](https://ws3.sinaimg.cn/large/006tNbRwgy1fxw90anl1yj31c00u0taw.jpg)

角色管理页面截图

![image-20181211214925677](https://ws4.sinaimg.cn/large/006tNbRwgy1fy358731qtj31c00u04lx.jpg)

资源管理页面截图（支持前端Vue Router `path`关联）

![image-20181207163547451](https://ws2.sinaimg.cn/large/006tNbRwgy1fy2tx2neg1j31hc0u0q9b.jpg)

![](https://ws4.sinaimg.cn/large/006tNbRwgy1fy7ui2yu4cj31c00u0438.jpg)

接口管理页面截图

# 快速开始

+ [导入数据【sql-mysql.sql】](https://github.com/Jiiiiiin/jiiiiiin-security/blob/master/db/sql-mysql.sql)
+ [启动redis，配置查看【application.yml#redis】](https://github.com/Jiiiiiin/jiiiiiin-security/blob/master/jiiiiiin-server-manager/src/main/resources/application.yml#L28)
+ 启动后端内管应用
+ 进入前端内管应用：jiiiiiin-client-manager
    - 安装项目依赖：`npm i`
    - 配置服务端uri：`.env.development`文件下：`VUE_APP_SEVER_URL=http://192.168.1.123:9000`
    - 启动前端内管应用：`npm run serve`

# 所用技术栈

### 后台
    
+ [springboot](https://github.com/spring-projects/spring-boot)

+ [spring security](https://github.com/spring-projects/spring-security)

+ [mybatis-plus](https://github.com/baomidou/mybatis-plus)

### 前端    
    
+ [vue](https://github.com/vuejs/vue)

+ [ElemeFE/element](https://github.com/ElemeFE/element)

+ [vue-viewplus](https://github.com/Jiiiiiin/vue-viewplus)

+ [d2-admin](https://gi]thub.com/d2-projects/d2-admin)

<a href="https://github.com/d2-projects/d2-admin" target="_blank"><img src="https://raw.githubusercontent.com/FairyEver/d2-admin/master/doc/image/d2-admin@2x.png" width="200"></a>

  
  
