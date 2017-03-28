/**
 * 
 */
package com.hgsoft.obd.quartz;

import java.util.Date;

import javax.annotation.Resource;

import com.hgsoft.carowner.entity.OBDStockInfo;
import com.hgsoft.carowner.service.OBDStockInfoService;
/**
 * @author Administrator
 *
 */
public class MyJob{
	@Resource
	private OBDStockInfoService obdStockInfoService;

	public void work() {  
//        System.out.println("date:" + new Date().toString());  
//        OBDStockInfo obd = obdStockInfoService.find("1459251955412wmf06");
//        System.out.println(obd);
    }  
}
