package org.jwebap.plugin.tracer.method;

import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.json.JSONString;
import org.jwebap.core.StatistableTrace;
import org.jwebap.core.Trace;
import org.jwebap.core.TraceKey;
import org.jwebap.util.Arrays;

public class MethodTrace extends StatistableTrace implements JSONString {

	private int i = 0;
	
	private int mcget = 0;
	private int mcset = 0;
	private int mcdel = 0;

	private String _method = null;

	private Object[] _args = null;

	public MethodTrace(Trace stackTrace, Object proxy, Method method, Object[] args) {
		super(stackTrace);
		ini(proxy, method, args);
	}

	public MethodTrace(Object proxy, Method method, Object[] args) {
		ini(proxy, method, args);
	}

	private void ini(Object proxy, Method method, Object[] args) {

		String methodName = method.toString();

		setMethod(methodName);

		setArgs(args);

		StatistableTrace.InnerKey key = new StatistableTrace.InnerKey(methodName);

		setKey(key);
	}

	public void openConnection() {
		i++;
	}

	public int getOpenedConnectionCount() {
		return i;
	}
	
	public void mcGet() {
		mcget ++;
	}
	
	public int getMcGetCount() {
		return mcget;
	}
	
	public void mcSet() {
		mcset ++;
	}
	
	public int getMcSetCount() {
		return mcset;
	}
	
	public void mcDel() {
		mcdel ++;
	}
	
	public int getMcDelCount() {
		return mcdel;
	}

	public Object[] getArgs() {
		return _args;
	}

	public void setArgs(Object[] args) {
		if (args != null) {
			this._args = args;
		} else {
			this._args = new Object[0];
		}
	}

	public String getMethod() {
		return _method;
	}

	public void setMethod(String method) {
		this._method = method;
	}

	/**
	 * 返回轨迹对应的Json对象，供视图使用
	 * 
	 * @return
	 */
	public String toJSONString() {
		Map<String, String> map = new HashMap<String, String>();

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String createTime = "--:--";
		String destroyTime = "--:--";
		if (getCreatedTime() > 0) {
			createTime = format.format(new Timestamp(getCreatedTime()));
		}
		if (getInactiveTime() > 0) {
			destroyTime = format.format(new Timestamp(getInactiveTime()));
		}
		map.put("isClosed", getInactiveTime() > 0 ? "closed" : "opened");
		map.put("cost", String.valueOf(getActiveTime()));
		map.put("createTime", createTime);
		map.put("destoryTime", destroyTime);
		map.put("method", getMethod());
		map.put("jdbcOpened", String.valueOf(getOpenedConnectionCount()));
		map.put("mcGet", String.valueOf(getMcGetCount()));
		map.put("mcSet", String.valueOf(getMcSetCount()));
		map.put("mcDel", String.valueOf(getMcDelCount()));
		map.put("mcTotal", String.valueOf(getMcGetCount() + getMcSetCount() + getMcDelCount()));
		// http轨迹的详细信息
		String detail = getStackTracesDetail();

		String lf = "<br/>";

		map.put("detail", detail.replaceAll("\n", lf));

		String thread = "";
		thread = ((TraceKey) getKey()).getThreadKey().toString();
		map.put("thread", thread);

		String arguments = "";
		Object[] args = getArgs();
		for (int i = 0; i < args.length; i++) {
			if (args[i] == null) {
				arguments += "null" + lf;
			} else if (args[i] instanceof Object[]) {
				arguments += args[i].getClass().getName() + " : " + Arrays.deepToString((Object[]) args[i]) + lf;
			} else {
				arguments += args[i].getClass().getName() + " : " + args[i] + lf;
			}
		}
		map.put("args", arguments);

		return new JSONObject(map).toString();
	}

}
