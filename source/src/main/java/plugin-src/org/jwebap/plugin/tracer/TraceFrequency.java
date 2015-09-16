package org.jwebap.plugin.tracer;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.json.JSONString;
import org.jwebap.core.TraceKey;

/**
 * @author leadyu
 * @since Jwebap 0.5
 * @date Aug 12, 2007
 */
public class TraceFrequency implements JSONString {

	private TraceKey key;

	private int frequency;

	private long createTime;

	private long lastProcessTime;

	private long minActiveTime;

	private long maxActiveTime;

	private long totalActiveTime;

	public TraceFrequency() {
		key = null;
		frequency = 0;
		createTime = System.currentTimeMillis();
		lastProcessTime = System.currentTimeMillis();
		minActiveTime = -1L;
		maxActiveTime = -1L;
		totalActiveTime = 0L;
	}

	public TraceKey getKey() {
		return key;
	}

	public void setKey(TraceKey key) {
		this.key = key;
	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
		setLastProcessTime(System.currentTimeMillis());
	}

	public String toString() {
		return key != null ? key.getInvokeKey().toString() : null;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getLastProcessTime() {
		return lastProcessTime;
	}

	public void setLastProcessTime(long lastProcessTime) {
		this.lastProcessTime = lastProcessTime;
	}

	public long getMaxActiveTime() {
		return maxActiveTime;
	}

	public void setMaxActiveTime(long maxActiveTime) {
		this.maxActiveTime = maxActiveTime;
	}

	public long getMinActiveTime() {
		return minActiveTime;
	}

	public void setMinActiveTime(long minActiveTime) {
		this.minActiveTime = minActiveTime;
	}

	public long getTotalActiveTime() {
		return totalActiveTime;
	}

	public void setTotalActiveTime(long totalActiveTime) {
		this.totalActiveTime = totalActiveTime;
	}

	/**
	 * 返回轨迹对应的Json对象，供视图使用
	 * 
	 * @return
	 */
	public String toJSONString() {
		Map<String, Object> map = new HashMap<String, Object>();

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String createTime = "--:--";
		String lastProcessTime = "--:--";
		if (getCreateTime() > 0) {
			createTime = format.format(new Timestamp(getCreateTime()));
		}
		if (getLastProcessTime() > 0) {
			lastProcessTime = format.format(new Timestamp(getLastProcessTime()));
		}
		map.put("createTime", createTime);
		map.put("key", getKey().getInvokeKey());
		map.put("frequency", String.valueOf(getFrequency()));
		map.put("last", lastProcessTime);
		map.put("min", String.valueOf(getMinActiveTime()));
		map.put("max", String.valueOf(getMaxActiveTime()));
		long average = (int) (getTotalActiveTime() / (long) getFrequency());
		map.put("average", String.valueOf(average));
		//判断是uri才评估分数
		if (String.valueOf(getKey().getInvokeKey()).contains("/")) {
			//判断性能分数
			double score = 0;
			String scoreGrade = "";
			if (average == 0) {
				score = 100;
			} else if (average < 4000) {
				score = 100 - (average * 0.025);
			} else {
				score = 0;
			}
			if (score < 0) {
				score = 0;
			}
			if (score >= 80 && score <= 100) {
				scoreGrade = "<font color='green'>响应良好</font>";
			} else if (score >= 60 && score < 80) {
				scoreGrade = "<font color='brown'>响应一般，有待优化</font>";
			} else if (score >= 0 && score < 60) {
				scoreGrade = "<font color='red'>响应较慢，请检查页面</font>";
			} else {
				scoreGrade = "--";
			}
			map.put("scoreGrade", scoreGrade);
		}
		return new JSONObject(map).toString();
	}
}
