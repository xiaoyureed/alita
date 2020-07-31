package io.github.xiaoyureed.alita.alitaioc.bean;

import io.github.xiaoyureed.alita.alitaioc.annotation.Bean;
import io.github.xiaoyureed.alita.alitaioc.annotation.Inject;

/**
 * @author xiaoyu
 * date: 2020/7/20
 */
@Bean("mama")
public class Mama {
    @Inject("son")
    private Son son;

    public Son getSon() {
        return son;
    }

    public void setSon(Son son) {
        this.son = son;
    }
}
