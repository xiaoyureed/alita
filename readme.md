# toy-ioc

基于注解的一个ioc容器

## quickstart

```java
@Bean("son")
public class Son {

}

@Bean("mama")
@Data// 需要引入lombok
public class Mama {

    @Inject("son")
    private Son son;
    
    // lombok 提供 getter
}
```

使用:

```java
public class DefaultContainerTest {

    @Test
    public void testDefaultContainer() {
        DefaultContainer container = DefaultContainer.me();
        container.register("com.xiaoyu.ioc.bean");
        Object oMama = container.getByName("mama");
        System.out.println(oMama);
    }
}
```

# mvc

Java实现的一个mvc框架, 基于 servlet, 使用 Filter 实现控制器功能, 借助 reflect 特性实现方法的动态调用, 开放一个接口 `MvcInitializer` 用来做框架的初始化.

核心思路:

- 提供一个全局配置类(singleton), 存储所有路由, 渲染器, 可能还有别的其他一些配置

- 提供一个 filter 做整个框架的初始化(比如设置路由, 设置渲染器...), 拦截每个 request, 获取其 uri, 和所有路由比对, 如果匹配上, 则执行controller中的动作, 如果没有则放行

- 抽出一个接口, 只一个方法 init(xxx), 开放给外界, 用于提供初始化的数据.

## quickstart

见子模块 sample, 启动脚本: start_sample.sh, 访问: localhost:8082

创建一个web项目, 新建controller类

```java
public class IndexController {

    public void index(Request req, Response resp) {
        System.out.println("indexController");
        resp.getRender().render("index");
    }
}
```

新建一个初始化类, 实现 `MvcInitializer` 接口

```java
public class App implements MvcInitializer {

    @Override
    public void init(Mvc mvc) {
        IndexController index = new IndexController();
        mvc.add("/", "index", index);
    }

}
```

web.xml配置

```xml
<!DOCTYPE web-app PUBLIC
 "-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN"
 "http://java.sun.com/dtd/web-app_2_3.dtd" >

<web-app>
  <display-name>Archetype Created Web Application</display-name>
  <filter>
    <filter-name>mvc</filter-name>
    <filter-class>io.github.xiaoyureed.filter.CoreFilter</filter-class>
    <init-param>
      <param-name>init</param-name>
      <param-value>io.github.xiaoyureed.App</param-value>
    </init-param>
  </filter>

  <filter-mapping>
    <filter-name>mvc</filter-name>
    <url-pattern>/*</url-pattern>
  </filter-mapping>
</web-app>

```

当然还需要一个index.jsp页面, 默认在 `WEB-INF`下.





