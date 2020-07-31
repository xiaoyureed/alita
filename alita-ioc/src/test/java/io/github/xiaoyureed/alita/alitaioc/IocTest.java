package io.github.xiaoyureed.alita.alitaioc;

import io.github.xiaoyureed.alita.alitaioc.bean.Mama;
import io.github.xiaoyureed.alita.alitaioc.impl.DefaultAlitaContainer;
import org.junit.Test;

/**
 * @author xiaoyu
 * date: 2020/7/20
 */
public class IocTest {
    @Test
    public void testDefaultContainer() {
        DefaultAlitaContainer container = DefaultAlitaContainer.me();
        container.register("io.github.xiaoyureed.alita.alitaioc.bean");
        Object oMama = container.getByName("mama");
        System.out.println(oMama);
        System.out.println(((Mama) oMama).getSon());
    }
}

