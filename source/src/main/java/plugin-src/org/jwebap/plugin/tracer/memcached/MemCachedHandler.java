package org.jwebap.plugin.tracer.memcached;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.jwebap.toolkit.bytecode.asm.MethodInjectHandler;
/**
 * @author zhongwm <zhongweimin@pconline.com.cn>
 * Memcached¼à¿Ø×¢Èëhandler
 */
public class MemCachedHandler implements MethodInjectHandler {
	
	private MemCachedEventListener[] _listeners = null;
	
	public MemCachedHandler(MemCachedEventListener[] listeners) {
		_listeners = listeners;
	}

	@Override
	public Object invoke(Object target, Method method, Method methodProxy,
			Object[] args) throws Throwable {	
		Object object;
		try {
			object = methodProxy.invoke(target, args);
		} catch (InvocationTargetException e) {
			throw e.getCause();
		} finally {
		}
		if ("get".equals(method.getName()) || "getMulti".equals(method.getName())) {
			for(MemCachedEventListener listener : _listeners) {
				listener.get();
			}
		} else if ("add".equals(method.getName()) || "set".equals(method.getName())) {
			for(MemCachedEventListener listener : _listeners) {
				listener.set();
			}			
		} else if ("delete".endsWith(method.getName())) {
			for(MemCachedEventListener listener : _listeners) {
				listener.del();
			}
		}
		//System.err.println("------" + method.getName() + " ----------- " + target.getClass().getSimpleName());	
		return object;
		
	}

}
