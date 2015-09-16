package org.jwebap.plugin.tracer.jdbc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.jwebap.plugin.tracer.FrequencyAnalyser;
import org.jwebap.plugin.tracer.util.TraceStatViewHelper;
import org.jwebap.ui.controler.JSONActionSupport;
import org.jwebap.util.Assert;

/**
 * jdbc plugin time filter JDBC异步请求超时轨迹数据的响应Action
 * 
 * @author leadyu(yu-lead@163.com)
 * @since Jwebap 0.5
 * @date 2008-1-11
 */
public class StatDatasAction extends JSONActionSupport {

	public JSONObject processJson(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		/** 获得http请求轨迹分析器，以获得分析数据 */
		FrequencyAnalyser analyser = JdbcComponent.frequencyAnalyser;
		Assert.assertNotNull(analyser,
				"HttpComponent is not startup:stat analyser is null.");

		TraceStatViewHelper helper = new TraceStatViewHelper(analyser);

		return helper.processJson(request, response);
	}

}
