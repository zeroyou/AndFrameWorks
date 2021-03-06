package com.andframe.annotation.view.idname;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 注解式绑定控件<br>
 *  @ BindClick$("viewId")
 *  public void onClick(View v) {
 *  }
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BindClick$ {
    String[] value();
    int intervalTime() default 1000;
}

