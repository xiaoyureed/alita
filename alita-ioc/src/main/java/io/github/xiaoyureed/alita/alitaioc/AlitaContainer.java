package io.github.xiaoyureed.alita.alitaioc;

/**
 * @author xiaoyu
 * date: 2020/7/20
 */
public interface AlitaContainer {

    /**
     * 扫描所有被标注的 bean
     */
    void register(String basePackage);

    Object getByName(String name);

    <T> T getByType(Class<T> clazz);
}
