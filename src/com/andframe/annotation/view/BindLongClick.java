package com.andframe.annotation.view;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 注解式绑定控件<br>
    @ BindLongClick(R.id.viewId)
    public boolean onLongClick(View v) {
    }
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BindLongClick {
    public int[] value();
}

