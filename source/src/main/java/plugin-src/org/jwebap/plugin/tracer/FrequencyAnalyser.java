package org.jwebap.plugin.tracer;

import java.util.Hashtable;
import java.util.Map;

import org.jwebap.core.Analyser;
import org.jwebap.core.StatistableTrace;
import org.jwebap.core.StatistableTrace.InnerKey;
import org.jwebap.core.Trace;
import org.jwebap.core.TraceKey;

/**
 * Ƶ�ʷ�����
 * ���ݹ켣key��ͳ�ƹ켣���õ�Ƶ��
 * 
 * @author leadyu
 * @since Jwebap 0.5
 * @date Aug 12, 2007
 */
public class FrequencyAnalyser implements Analyser {

	protected Map<Object, TraceFrequency> pageFrequencies;

	public FrequencyAnalyser() {
		pageFrequencies = new Hashtable<Object, TraceFrequency>();
	}

	public void activeProcess(Trace trace) {

	}

	public void inactiveProcess(Trace trace) {
		if (!(trace instanceof StatistableTrace)) {
			return;
		}
		TraceKey key = (TraceKey) ((StatistableTrace) trace).getKey();
		long activeTime = System.currentTimeMillis() - trace.getCreatedTime();
		// ���û��Invokekey�򲻽���Ƶ��ͳ��
		if (key != null && key.getInvokeKey() != null) {
			TraceFrequency fq = (TraceFrequency) pageFrequencies.get(key.getInvokeKey());
			if (fq != null) {
				fq.setFrequency(fq.getFrequency() + 1);
			} else {
				fq = new TraceFrequency();
				fq.setKey(key);
				fq.setFrequency(fq.getFrequency() + 1);
				pageFrequencies.put(key.getInvokeKey(), fq);
			}
			if (fq.getMinActiveTime() == -1L) {
				fq.setMinActiveTime(activeTime);
				fq.setMaxActiveTime(activeTime);
			} else if (activeTime < fq.getMinActiveTime())
				fq.setMinActiveTime(activeTime);
			else if (activeTime > fq.getMaxActiveTime())
				fq.setMaxActiveTime(activeTime);
			fq.setTotalActiveTime(fq.getTotalActiveTime() + activeTime);
		}

	}
	
	public void inactiveProcess(Object key, long activeTime) {
		TraceFrequency fq = (TraceFrequency) pageFrequencies.get(key);
		if (fq != null) {
			fq.setFrequency(fq.getFrequency() + 1);
		} else {
			fq = new TraceFrequency();
			fq.setKey(new InnerKey(key));
			fq.setFrequency(fq.getFrequency() + 1);
			pageFrequencies.put(key, fq);
		}
		if (fq.getMinActiveTime() < 0L) {
			fq.setMinActiveTime(activeTime);
			fq.setMaxActiveTime(activeTime);
		} else if (activeTime < fq.getMinActiveTime()) {
			fq.setMinActiveTime(activeTime);
		} else if (activeTime > fq.getMaxActiveTime()) {
			fq.setMaxActiveTime(activeTime);
		}
		fq.setTotalActiveTime(fq.getTotalActiveTime() + activeTime);
	}

	public void destoryProcess(Trace trace) {

	}

	public void clear() {
		pageFrequencies.clear();

	}

	public Map<Object, TraceFrequency> getTraceFrequencies() {
		return pageFrequencies;
	}

}
