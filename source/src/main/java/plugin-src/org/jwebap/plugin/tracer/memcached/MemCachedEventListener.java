package org.jwebap.plugin.tracer.memcached;

/**
 * @author zhongwm <zhongweimin@pconline.com.cn>
 * MemCached事件监听接口
 */
public interface MemCachedEventListener {
	public void get();
	public void set();
	public void del();
}
