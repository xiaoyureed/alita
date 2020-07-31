package io.github.xiaoyureed.alita.alitamvc.wrapper;

import io.github.xiaoyureed.alita.alitamvc.Render;

import javax.servlet.ServletResponse;

/**
 * @Auther: xiaoyu
 * @Date: 2018/9/19 10:11
 * @Description: servlet response wrapper
 */
public class MvcResponse {

    private ServletResponse raw;

    private Render render;

    public ServletResponse getRaw() {
        return raw;
    }

    public void setRaw(ServletResponse raw) {
        this.raw = raw;
    }

    public Render getRender() {
        return render;
    }

    public void setRender(Render render) {
        this.render = render;
    }

    public MvcResponse(ServletResponse raw, Render render) {
        this.render = render;
        this.raw = raw;
    }

    public void render(String view) {
        this.render.render(view);
    }

}
