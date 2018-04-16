package com.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author cristian.serje
 * @since 2018.01.14
 */
@Target(ElementType.TYPE) // on class level
@Retention(RetentionPolicy.SOURCE) // not needed at runtime
public @interface BundleBind {
}
