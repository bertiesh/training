package com.example.training.annotation;

import java.lang.annotation.*;

/**
 * redis limitation
 * @author Xinyuan Xu
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AccessLimit {
    /**
     * time unit（seconds）
     *
     * @return int
     */
    int seconds();

    /**
     * maximum requests per time unit
     *
     * @return int
     */
    int maxCount();
}
