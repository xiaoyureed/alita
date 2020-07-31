package io.github.xiaoyureed.alita.alitamvc;

import io.github.xiaoyureed.alita.alitamvc.impl.JspRender;
import io.github.xiaoyureed.alita.alitamvc.route.Route;
import io.github.xiaoyureed.alita.alitamvc.route.RouteList;

/**
 * @Auther: xiaoyu
 * @Date: 2018/9/18 23:31
 * @Description: store routes, render, config etc...; singleton, 初始化完毕, 在整个应用生命周期内就不变了
 */
public class Store {
    public boolean isInit() {
        return init;
    }

    public void setInit(boolean init) {
        this.init = init;
    }

    public RouteList getRouteList() {
        return routeList;
    }

    public void setRouteList(RouteList routeList) {
        this.routeList = routeList;
    }

    public Render getRender() {
        return render;
    }

    public void setRender(Render render) {
        this.render = render;
    }

    /**
     * whether it has been initialized
     */
    private boolean init = false;

    /**
     * 路由列表
     */
    private RouteList routeList;

    /**
     * render
     */
    private Render render;

    private Store() {
        routeList = new RouteList();
        render = new JspRender(); // take jsp render as default renderer

    }

    private static final class Holder {
        private static Store instance = new Store();
    }
    public static Store me() {
        return Store.Holder.instance;
    }

    public boolean addRoute(Route route) {
        return routeList.add(route);
    }

    public boolean addRoute(String path, String method, Object controller) {
        Route route = new Route(path, method, controller);
        return addRoute(route);
    }
}
