package org.jwebap.plugin.tracer.memcached;

/**
 * @author zhongwm <zhongweimin@pconline.com.cn>
 * MemCached�¼������ӿ�
 */
public interface MemCachedEventListener {
	public void get();
	public void set();
	public void del();
}
