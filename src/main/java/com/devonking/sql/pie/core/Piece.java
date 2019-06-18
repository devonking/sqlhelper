package com.devonking.sql.pie.core;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Piece {
    String value();
    String alias() default "";
}
