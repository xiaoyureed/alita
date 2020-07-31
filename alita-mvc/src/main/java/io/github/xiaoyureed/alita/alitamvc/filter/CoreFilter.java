package io.github.xiaoyureed.alita.alitamvc.filter;

import io.github.xiaoyureed.alita.alitacommon.util.MessageUtils;
import io.github.xiaoyureed.alita.alitacommon.util.ReflectUtil;
import io.github.xiaoyureed.alita.alitamvc.Initializer;
import io.github.xiaoyureed.alita.alitamvc.MvcThreadLocal;
import io.github.xiaoyureed.alita.alitamvc.Store;
import io.github.xiaoyureed.alita.alitamvc.consts.Constants;
import io.github.xiaoyureed.alita.alitamvc.route.Route;
import io.github.xiaoyureed.alita.alitamvc.wrapper.MvcRequest;
import io.github.xiaoyureed.alita.alitamvc.wrapper.MvcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: xiaoyu
 * @Date: 2018/9/19 00:05
 * @Description: initialize system & filter request
 */
public class CoreFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(CoreFilter.class);

    /**
     * initialize route, renderer(default: jsp renderer),
     * @param fc
     * @throws ServletException
     */
    @Override
    public void init(FilterConfig fc) throws ServletException {
        Store store = Store.me();
        if (store.isInit()) {// if store has been initialized
            return;
        }

        String init = fc.getInitParameter(Constants.INIT_PARAM_FILTER);
        log.debug(">>> load initializer: [{}]", init);
        Object o = ReflectUtil.createInstance(init);
        if (null == o) {
            throw new RuntimeException(MessageUtils.format(">>> init class was wrong: [{}]", init));
        }
        if (!(o instanceof Initializer)) {
            throw new RuntimeException(MessageUtils.format(">>> init class was wrong: [{}]", o.getClass().getName()));
        }
        Initializer initializer = (Initializer) o;
        initializer.init(store);

        store.setInit(true);

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.debug(">>> CoreFilter.doFilter");
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=utf-8");

        String uri   = req.getRequestURI();
        Store  store = Store.me();
        Route  route = store.getRouteList().find(uri);
        if (null == route) {
            filterChain.doFilter(req, resp);
            return;
        }

        MvcThreadLocal.set(Constants.CONTEXT_KEY_REQUEST, req);
        MvcThreadLocal.set(Constants.CONTEXT_KEY_RESPONSE, resp);

        try {
            // execute methods.
            executeRoute(req, resp, route);
        } catch (InvocationTargetException | IllegalAccessException e) {
            //e.printStackTrace();
            log.error(">>> something goes wrong while execute controller, uri: ["+uri+"]", e);
        }
    }

    private void executeRoute(HttpServletRequest req, HttpServletResponse resp, Route route) throws InvocationTargetException, IllegalAccessException {
        Object controller = route.getController();
        Method method = route.getMethod();

        Class<?>[] parameterTypes = method.getParameterTypes();// method params
        List argsTemp = new ArrayList(); // 临时装方法的参数
        for (Class c : parameterTypes) {
            if (c.getName().equals(MvcRequest.class.getName())) {
                argsTemp.add(new MvcRequest(req));
            }
            if (c.getName().equals(MvcResponse.class.getName())) {
                argsTemp.add(new MvcResponse(resp, Store.me().getRender()));
            }
        }
        Object[] args = argsTemp.toArray(new Object[argsTemp.size()]);

        method.invoke(controller, args);
    }

    @Override
    public void destroy() {
        MvcThreadLocal.clear();// 清除 threadLocal
    }
}
