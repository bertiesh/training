package com.example.training.annotation;

import java.lang.annotation.*;

/**
 * operation log
 * @author Xinyuan Xu
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OptLog {
    /**
     * @return operation type
     */
    String optType() default "";
}
