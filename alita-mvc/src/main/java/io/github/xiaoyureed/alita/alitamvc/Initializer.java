package io.github.xiaoyureed.alita.alitamvc;

/**
 * @Auther: xiaoyu
 * @Date: 2018/9/19 00:49
 * @Description: initializer
 */
public interface Initializer {

    /**
     * initialize framework, put route list, render into Store
     * @param store
     */
    void init(Store store);
}
