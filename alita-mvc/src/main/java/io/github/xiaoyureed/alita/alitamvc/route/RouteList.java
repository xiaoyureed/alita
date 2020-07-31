package io.github.xiaoyureed.alita.alitamvc.route;

import io.github.xiaoyureed.util.ExceptionUtils;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: xiaoyu
 * @Date: 2018/9/18 23:45
 * @Description: store all route
 */
@Data
public class RouteList {

    private List<Route> routeList;

    public RouteList() {
        routeList = new ArrayList<>();
    }
    public boolean add(Route route) {
        return this.routeList.add(route);
    }

    public boolean remove(Route route) {
        return this.routeList.remove(route);
    }

    public Route find(String path) {
        if (routeList.size() == 0) {
            ExceptionUtils.runtimeException(">>> route didn't initialized");
        }
        for (Route r: routeList) {
            if (r.getPath().equals(path)) {
                return r;
            }
        }
        //ExceptionUtils.runtimeException(">>> route not found");
        return null;
    }

}
