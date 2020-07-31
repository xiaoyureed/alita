package io.github.xiaoyureed.alita.alitamvc.wrapper;

import javax.servlet.ServletRequest;

/**
 * @Auther: xiaoyu
 * @Date: 2018/9/19 10:10
 * @Description: servlet request wrapper
 */
public class MvcRequest {

    public ServletRequest getRaw() {
        return raw;
    }

    public void setRaw(ServletRequest raw) {
        this.raw = raw;
    }

    private ServletRequest raw;

    public MvcRequest(ServletRequest raw) {
        this.raw = raw;
    }
}
