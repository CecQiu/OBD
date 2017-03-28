package com.hgsoft.common.entity;

import java.util.Queue;

/**
 * 系统参数基础类
 * @author fdf
 */
public final class BaseStatus {
	
	/**平台管理员角色*/
	public final static String ROLE_ADMIN = "平台管理员";
	/**彩站管理员角色*/
	public final static String ROLE_SHOPMANAGER = "彩站管理员";
	/**彩民角色*/
	public final static String ROLE_LOTTERY = "彩民";
	/**未知角色*/
	public final static String ROLE_NULL = "未知角色";
	
	/**故障情况_代码*/
	public final static String code_fault_status = "fault_status";
	/**故障情况 - 没故障*/
	public final static String type_fault_normal = "noFault";
	/**故障情况 - 有故障*/
	public final static String type_fault_error = "isFault";
}
