package io.github.xiaoyureed.alita.alitaioc.annotation;

import java.lang.annotation.*;

/**
 * inject resources into current object
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Inject {

    String value();
}
