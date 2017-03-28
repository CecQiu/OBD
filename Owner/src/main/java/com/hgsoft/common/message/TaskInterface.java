package com.hgsoft.common.message;

/**
 * 协议接口的任务接口类
 * @author fdf
 */
public interface TaskInterface {

	/**执行接口任务*/
	public abstract void doTask(OBDMessage om) throws Exception;
}
