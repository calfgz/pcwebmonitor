package org.jwebap.plugin.tracer.method;

import java.util.ArrayList;
import java.util.List;

import org.jwebap.plugin.tracer.memcached.MemCachedEventListener;

/**
 * @author zhongwm <zhongweimin@pconline.com.cn>
 * Method²Ù×÷MC¼àÌýÆ÷
 */
public class MethodMemCachedListener implements MemCachedEventListener {
	
	private static ThreadLocal<List<MethodTrace>> detectSeed = new ThreadLocal<List<MethodTrace>>();

	@Override
	public void get() {
		if (detectSeed.get() == null) {
			return;
		}
		
		try {
			List<MethodTrace> seeds = detectSeed.get();
			for (MethodTrace trace : seeds) {
				trace.mcGet();
			}
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
			List<MethodTrace> seeds = detectSeed.get();
			for (MethodTrace trace : seeds) {
				trace.mcSet();
			}
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
			List<MethodTrace> seeds = detectSeed.get();
			for (MethodTrace trace : seeds) {
				trace.mcDel();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public static void addDetectSeed(MethodTrace trace) {
		if (detectSeed != null) {
			List<MethodTrace> seeds = detectSeed.get();
			if (seeds == null) {
				seeds = new ArrayList<MethodTrace>();
				detectSeed.set(seeds);
			}
			seeds.add(trace);

		}
	}

	public static void removeDetectSeed(MethodTrace trace) {
		if (detectSeed != null) {
			List<MethodTrace> seeds = detectSeed.get();
			if (seeds != null) {
				seeds.remove(trace);
			}

		}
	}

}
