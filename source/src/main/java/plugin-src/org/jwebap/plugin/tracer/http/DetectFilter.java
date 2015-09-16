package org.jwebap.plugin.tracer.http;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jwebap.core.StatistableTrace;
import org.jwebap.core.TraceLiftcycleManager;

/**
 * ������������
 * @author ������
 *
 * 2007-4-5
 */
public class DetectFilter implements Filter {

	private static Log log = LogFactory.getLog(DetectFilter.class);
	   
	private FilterConfig _filterConfig;
	
	private transient static TraceLiftcycleManager CONTAINER=null;
	
	private String charset;

	
	private static TraceLiftcycleManager getContainer(){
		return CONTAINER;
	}
	
	public static void setContainer(TraceLiftcycleManager container){
		CONTAINER=container;
	}
	
	/**
	 * ��ʼ��
	 */
	public void init(FilterConfig filterConfig) throws ServletException {
		_filterConfig = filterConfig;
		String charset = filterConfig.getInitParameter("charset");
		if (charset == null || "".equals(charset.trim())) {
			charset = "gbk";
		}
		this.charset = charset; 
	}

	public String getInitParameter(String name) {
		return _filterConfig.getInitParameter(name);
	}

	public boolean doExclude(ServletRequest request, ServletResponse response, FilterChain filterChain) {
		try {
			String excludeUrls = getInitParameter("excludeUrls");
			String excludes[] = excludeUrls.split(";");
			HttpServletRequest httprequest = (HttpServletRequest) request;
			for (int i = 0; i < excludes.length; i++) {
				String path = httprequest.getRequestURI();
				String contextPath=httprequest.getContextPath();
				String regx = excludes[i].replaceAll("\\.", "\\\\.");
				regx = regx.replaceAll("\\*", "\\.*");
				if (excludes[i].endsWith("/"))
					regx = regx + ".*";
				
				if(regx.startsWith("/")){
					regx=regx.replaceFirst("/",contextPath+"/");
					regx=regx.replaceAll("//","/");
				}
				if (path.matches(regx))
					return true;
			}

		} catch (Throwable t) {
		}
		return false;
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		request.setCharacterEncoding(charset); 
		HttpServletRequest req = (HttpServletRequest) request;
		if (getContainer() == null || doExclude(request, response, filterChain)) {
			filterChain.doFilter(request, response);
			return;
		}
		HttpRequestTrace pageTrace = new HttpRequestTrace(req);
		try {
			try {
				getContainer().activateTrace(HttpComponent.HTTP_TRACE_TYPE,pageTrace);

				//��ʼ�����켣�����ݿ����
				ServletOpenedConnectionListener.addDetectSeed(pageTrace);
				
				ServletMemCachedListener.addDetectSeed(pageTrace);
				
			} catch (Throwable e) {
				log.warn(e.getMessage());
			}
			
			try {
				filterChain.doFilter(request, response);
	        } catch (ServletException se) {
	        	pageTrace.error(); throw se;
	        } catch (IOException ioe) {
	        	pageTrace.error(); throw ioe;
	        } catch (RuntimeException ex) {
	        	pageTrace.error(); throw (RuntimeException)ex;
	        } catch (Error err) {
	        	pageTrace.error(); throw err;
	        }
		} finally {
			try {
				getContainer().inactivateTrace(HttpComponent.HTTP_TRACE_TYPE,pageTrace);
				ServletOpenedConnectionListener.clearDetectSeed();
				ServletMemCachedListener.clearDetectSeed();
				//�������й켣���̱߳�ʶ������ʹ�����̳߳أ�������Ҫ���̱߳�����Դ���л��գ�
				StatistableTrace.InnerKey.clearThreadKey();
				
			} catch (Throwable e) {
				log.warn(e.getMessage());
			}
		}
		return;
	}

	public void destroy() {
	}



}
