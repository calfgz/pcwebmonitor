package org.jwebap.plugin.tracer.method;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.jwebap.plugin.tracer.jdbc.ConnectionEventListener;

public class MethodOpenedConnectionListener implements ConnectionEventListener {

	private static ThreadLocal<List<MethodTrace>> detectSeed = new ThreadLocal<List<MethodTrace>>();

	public void fire(Connection conn) {
		if (detectSeed.get() == null) {
			return;
		}

		try {
			List<MethodTrace> seeds = detectSeed.get();
			for (int i = 0; i < seeds.size(); i++) {
				MethodTrace trace = seeds.get(i);
				trace.openConnection();
			}
		} catch (Exception e) {

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
