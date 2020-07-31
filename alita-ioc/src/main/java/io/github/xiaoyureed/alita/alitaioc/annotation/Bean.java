package io.github.xiaoyureed.alita.alitaioc.annotation;

import java.lang.annotation.*;

/**
 * put beans into the Ioc container
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Bean {

    String value() default "";
}
