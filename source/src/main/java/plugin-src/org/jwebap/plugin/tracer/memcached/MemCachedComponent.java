package org.jwebap.plugin.tracer.memcached;

import java.util.List;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jwebap.core.Component;
import org.jwebap.core.ComponentContext;
import org.jwebap.toolkit.bytecode.ClassEnhancer;

/**
 * @author zhongwm <zhongweimin@pconline.com.cn>
 * MemCached监控组件
 */
public class MemCachedComponent implements Component {
	private static final Log log = LogFactory.getLog(MemCachedComponent.class);
	
	private ComponentContext componentContext = null;
	
	List<MemCachedEventListener> _memCachedListeners;
	
	public MemCachedComponent() {
		_memCachedListeners = new Vector<MemCachedEventListener>();
	}

	@Override
	public void startup(ComponentContext context) {
		componentContext = context;
		
		//加载监听器
		String listenerClasses = context.getProperty("memcached-listener");
		listenerClasses = listenerClasses.replaceAll("\n", "");
		listenerClasses = listenerClasses.replaceAll("\r", "");
		listenerClasses = listenerClasses.replaceAll("\t", "");
		listenerClasses = listenerClasses.trim();
		
		String[] lClasses = listenerClasses.split(";");
		
		for (String clazz : lClasses) {
			try {
				if (clazz.trim().length() > 0) {
					Class<?> listenerInstance = Class.forName(clazz);
					MemCachedEventListener listener = (MemCachedEventListener) listenerInstance.newInstance();
					addMemCachedListener(listener);
				}
			} catch (Exception e) {
				log.warn("create memcached listener " + clazz + " error. ");
				e.printStackTrace();
			}
		}
		
		injectMemcachedClient();
		
		log.info("memcachedcomponent startup.");		
	}
	
	public void addMemCachedListener(MemCachedEventListener listener) {
		_memCachedListeners.add(listener);
	}
	
	public void clearMemCachedListener() {
		_memCachedListeners.clear();
	}
	
	private void injectMemcachedClient() {
		ClassEnhancer enhancer = new ClassEnhancer();
		
		int size = _memCachedListeners.size();
		MemCachedEventListener[] listeners = new MemCachedEventListener[size];
		System.arraycopy(_memCachedListeners.toArray(), 0, listeners, 0, size);
		
		String mcClientName = "com.danga.MemCached.MemCachedClient";
		try {
			enhancer.createClass(mcClientName, new MemCachedHandler(listeners), true);
		} catch (Exception e) {
			e.printStackTrace();
			log.warn("对MemCachedClient类的监听部署失败,不过这并不影响系统的运行.");
		}
	}
	
	@Override
	public void destory() {
		
	}

	@Override
	public void clear() {
	}

	@Override
	public ComponentContext getComponentContext() {
		return componentContext;
	}

}
