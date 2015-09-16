package org.jwebap.plugin.tracer.http;

import org.jwebap.plugin.tracer.memcached.MemCachedEventListener;

/**
 * @author zhongwm <zhongweimin@pconline.com.cn>
 * HTTP²Ù×÷MC¼àÌýÆ÷
 */
public class ServletMemCachedListener implements MemCachedEventListener {
	
	private static ThreadLocal<HttpRequestTrace> detectSeed = new ThreadLocal<HttpRequestTrace>();
	
	@Override
	public void get() {
		if (detectSeed.get() == null) {
			return;
		}
		
		try {
			HttpRequestTrace trace = (HttpRequestTrace) detectSeed.get();
			trace.mcGet();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void set() {
		if (detectSeed.get() == null) {
			return;
		}

		try {
			HttpRequestTrace trace = (HttpRequestTrace) detectSeed.get();
			trace.mcSet();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void del() {
		if (detectSeed.get() == null) {
			return;
		}

		try {
			HttpRequestTrace trace = (HttpRequestTrace) detectSeed.get();
			trace.mcDel();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public static void addDetectSeed(HttpRequestTrace trace) {
		if (detectSeed != null) {
			detectSeed.set(trace);
		}
	}
	
	public static void clearDetectSeed() {
		if (detectSeed != null) {
			detectSeed.set(null);
		}
	}

}
