package org.jwebap.plugin.tracer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.jwebap.core.Analyser;
import org.jwebap.core.Trace;

public class TimeFilterAnalyser implements Analyser {

	protected int maxTraceSize;

	private List<Trace> traces;

	private long tracefilterActivetime;

	public TimeFilterAnalyser() {
		maxTraceSize = -1;
		traces = new Vector<Trace>();
		tracefilterActivetime = -1L;
	}

	public void activeProcess(Trace trace) {
		if (getMaxTraceSize() > -1 && traces.size() >= getMaxTraceSize()) {
			removeFirstTrace();
		}
		traces.add(trace);
	}

	public void inactiveProcess(Trace trace) {
		long activeTime = System.currentTimeMillis() - trace.getCreatedTime();

		if (trace != null && tracefilterActivetime >= 0L && tracefilterActivetime >= activeTime) {
			removeTrace(trace);
		} else {
			trace.inActive();
		}

	}

	public void destoryProcess(Trace trace) {

	}

	/**
	 * 清空统计数据
	 */
	public void clear() {
		List<Trace> children = new ArrayList<Trace>(getTraces());
		Trace trace;
		for (Iterator<Trace> it = children.iterator(); it.hasNext();) {
			trace = (Trace) it.next();
			trace.destroy();
		}
		traces.clear();
	}

	public List<Trace> getTraces() {
		return traces;
	}

	public long getTracefilterActivetime() {
		return tracefilterActivetime;
	}

	public void setTracefilterActivetime(long tracefilterActivetime) {
		this.tracefilterActivetime = tracefilterActivetime;
	}

	public int getMaxTraceSize() {
		return maxTraceSize;
	}

	public void setMaxTraceSize(int maxTraceSize) {
		this.maxTraceSize = maxTraceSize;
	}

	protected synchronized void removeFirstTrace() {
		if (traces != null && traces.size() > 0)
			removeTrace((Trace) traces.get(0));
	}

	protected synchronized void removeTrace(Trace trace) {
		if (traces != null) {
			traces.remove(trace);
			trace.destroy();
		}
	}
}