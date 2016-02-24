package com.andframe.layoutbind.framework;

import android.view.View;

import com.andframe.activity.framework.AfViewable;
import com.andframe.annotation.inject.interpreter.Injecter;
import com.andframe.annotation.view.BindLayout;
import com.andframe.application.AfApplication;
import com.andframe.application.AfExceptionHandler;
import com.andframe.annotation.interpreter.ViewBinder;

import java.lang.reflect.Constructor;

public class AfViewModule extends AfViewDelegate implements AfViewable,IViewModule{

	public static <T extends AfViewModule> T init(Class<T> clazz,AfViewable view,int viewId){
		try {
			T module = null;
			Constructor<?>[] constructors = clazz.getConstructors();
			for (int i = 0; i < constructors.length && module == null; i++) {
				Class<?>[] parameterTypes = constructors[i].getParameterTypes();
				if (parameterTypes.length == 0) {
					module = clazz.newInstance();
				} else if (parameterTypes.length == 1 && AfViewable.class.isAssignableFrom(parameterTypes[0])) {
					module = (T)constructors[i].newInstance(view);
				}
			}
			if (module != null) {
				AfViewModule viewModule = module;
				viewModule.setTarget(view.findViewByID(viewId));
			}
			return module;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	protected AfViewModule(){
		super(new View(AfApplication.getApp()));
	}

	protected AfViewModule(AfViewable view) {
		super(new View(view.getContext()));
		if (this.getClass().isAnnotationPresent(BindLayout.class)) {
			BindLayout bind = this.getClass().getAnnotation(BindLayout.class);
			target = view.findViewById(bind.value());
		}
	}

	protected AfViewModule(AfViewable view, int id) {
		super(new View(view.getContext()));
		target = view.findViewById(id);
	}

	private void setTarget(View target) {
		this.target = target;
		this.onCreated(target);
	}

	protected void onCreated(View target) {
		this.doInject();
	}

	protected void doInject(){
		if(isValid()){
			ViewBinder binder = new ViewBinder(this);
			binder.doBind(target);
			Injecter injecter = new Injecter(this);
			injecter.doInject(getContext());
		}
	}

	@Override
	public void hide() {
		if(isValid()){
			setVisibility(View.GONE);
		}
	}

	@Override
	public void show() {
		if(isValid()){
			setVisibility(View.VISIBLE);
		}
	}

	@Override
	public boolean isValid() {
		return target != null;
	}

	@Override
	public boolean isVisibility() {
		if(isValid()){
			return getVisibility() == View.VISIBLE;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends View> T findViewByID(int id) {
		try {
			return (T)target.findViewById(id);
		} catch (Throwable e) {
			AfExceptionHandler.handler(e, "AfViewModule.findViewByID");
		}
		return null;
	}

	@Override
	public <T extends View> T findViewById(int id, Class<T> clazz) {
		View view = target.findViewById(id);
		if (clazz.isInstance(view)) {
			return clazz.cast(view);
		}
		return null;
	}

}
