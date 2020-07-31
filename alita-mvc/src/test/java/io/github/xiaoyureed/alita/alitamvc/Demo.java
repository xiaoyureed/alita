package io.github.xiaoyureed.alita.alitamvc;

import org.junit.Test;

import java.text.MessageFormat;

/**
 * @author xiaoyu
 * date: 2020/7/20
 */
public class Demo {
    @Test
    public void testSplit() {
        String test = "{} nihoa {} xxx {}.";
        String[] split = test.split("\\{}");
        System.out.println("------"+ split.length +"--"+split[0].length()+"---");
        for (String s: split) {
            System.out.println(s);
        }
    }

    @Test
    public void testMessageFormat() {
        String str = MessageFormat.format("你好 [{0}],sdfs [{1}]", "param1", "param2");
        System.out.println(str);
    }
}
