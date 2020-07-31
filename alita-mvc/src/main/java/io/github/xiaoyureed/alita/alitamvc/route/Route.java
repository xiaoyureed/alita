package io.github.xiaoyureed.alita.alitamvc.route;

import io.github.xiaoyureed.exception.RouteCreateException;
import lombok.Data;

import java.lang.reflect.Method;
import java.text.MessageFormat;

/**
 * @Auther: xiaoyu
 * @Date: 2018/9/18 23:37
 * @Description: route
 */
@Data
public class Route {

    private String path;

    private Method method;

    private Object controller;

    public Route(String path, Method method, Object controller) {
        this.path = path;
        this.method = method;
        this.controller = controller;
    }

    public Route(String path, String methodName, Object controller) {
        this.path = path;
        this.controller = controller;

        Method[] methods = controller.getClass().getMethods();
        for (Method m: methods) {
            if (m.getName().equals(methodName)) {
                this.method = m;
                break;
            }
        }

        if (this.method == null) {
            throw new RouteCreateException(
                    MessageFormat.format("controller [{0}] 中没有方法 [{1}]", controller, methodName));
        }
    }
}
