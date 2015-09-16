package org.jwebap.plugin.tracer.http;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.json.JSONString;
import org.jwebap.core.StatistableTrace;
import org.jwebap.core.Trace;
import org.jwebap.util.Arrays;
public class HttpRequestTrace extends StatistableTrace implements JSONString {

	private int i = 0;
	
	private int mcget = 0;
	private int mcset = 0;
	private int mcdel = 0;
	
	private int errors = 0;

	private String ip = null;

	/** ����URI */
	private String uri = null;

	/** ����queryString */
	private String queryString = null;

	/** ����Ĳ����б� */

	private Map<?, ?> parameters = new HashMap<Object, Object>();

	public HttpRequestTrace(Trace stackTrace, HttpServletRequest request) {
		super(stackTrace);
		ini(request);
	}

	public HttpRequestTrace(HttpServletRequest request) {
		ini(request);
	}

	private void ini(HttpServletRequest request) {
		// uri
		setUri(request.getRequestURI());
		// queryString
		if (request.getQueryString() != null) {
			setQueryString(request.getQueryString());
		}
		// ip
		setIp(request.getRemoteHost());

		// ����
		setParameters(request.getParameterMap());

		// inner key:����ͳ�Ʒ���Ƶ�ʣ���uriΪkey��ͳ�Ʒ��ʴ�����ʱ��
		StatistableTrace.InnerKey key = new StatistableTrace.InnerKey(uri);
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
	
	public void error() {
		errors++;
	}
	
	public int getErrors() {
		return errors;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Map<?, ?> getParameters() {
		return parameters;
	}

	public void setParameters(Map<?, ?> parameters) {
		this.parameters = parameters;
	}

	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	/**
	 * ���ع켣��Ӧ��Json���󣬹���ͼʹ��
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
		map.put("ip", getIp());
		map.put("uri", getUri());
		map.put("jdbcOpened", String.valueOf(getOpenedConnectionCount()));
		map.put("errors", getErrors() > 0 ? "error":"");
		map.put("mcGet", String.valueOf(getMcGetCount()));
		map.put("mcSet", String.valueOf(getMcSetCount()));
		map.put("mcDel", String.valueOf(getMcDelCount()));
		map.put("mcTotal", String.valueOf(getMcGetCount() + getMcSetCount() + getMcDelCount()));
		//�ж����ܷ���
		double score = 0;
		String scoreGrade = "";
		if (getActiveTime() == 0) {
			score = 100;
		} else if (getActiveTime() < 4000) {
			score = 100 - (getActiveTime() * 0.025);
		} else {
			score = 0;
		}
		score = score - (errors * 10);
		if (score < 0) {
			score = 0;
		}
		if (score >= 80 && score <= 100) {
			scoreGrade = "<font color='green'>��Ӧ����</font>";
		} else if (score >= 60 && score < 80) {
			scoreGrade = "<font color='brown'>��Ӧһ�㣬�д��Ż�</font>";
		} else if (score >= 0 && score < 60) {
			scoreGrade = "<font color='red'>��Ӧ����������ҳ��</font>";
		} else {
			scoreGrade = "--";
		}
		score = Math.round(score);
		map.put("scoreGrade", scoreGrade);
		// http�켣����ϸ��Ϣ
		String params = "";
		Map<?, ?> parameters = getParameters();
		String lf = "<br/>";
		for (Iterator<?> keys = parameters.keySet().iterator(); keys.hasNext();) {
			Object key = keys.next();
			Object value = parameters.get(key);
			String strValue = "";
			if (value instanceof Object[]) {
				strValue = Arrays.deepToString((Object[]) value);
			} else {
				strValue = value.toString();
			}
			params += "  " + key + " : " + strValue + lf;
		}
		String detail = "";
		detail += "ip     :" + getIp() + lf + "uri    :" + getUri() + lf
				+ "query  :" + getQueryString() + lf + "params :" + "<hr/>"
				+ params + "����ҳ�棺" + getErrors() + "<br/>��Ӧ��ʱ��" + getActiveTime() + "ms<br/>"
				+ "MC��ȡ������" + getMcGetCount() + "<br/>"
				+ "MCд�������" + getMcSetCount() + "<br/>"
				+ "MCɾ��������" + getMcDelCount() + "<br/>"
				+ "����������" + score + "��<br/>" + scoreGrade;
		map.put("detail", detail);
		return new JSONObject(map).toString();
	}
}
