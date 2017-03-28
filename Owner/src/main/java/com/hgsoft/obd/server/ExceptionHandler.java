package com.hgsoft.obd.server;

import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class ExceptionHandler{
	
	private static Logger exceptionLogger = LogManager.getLogger("exceptionLogger");

	@AfterThrowing(throwing="e",pointcut="execution(* com.hgsoft.obd.util.*.*(..))"
			+ " || execution(* com.hgsoft.obd.service.*.*(..)) "
			+ " || execution(* com.hgsoft.carowner.dao.*.*(..))"
			+ " || execution(* com.hgsoft.carowner.service.*.*(..)) ")
	public void afterThrowing(JoinPoint joinPoint,Throwable e) {
        String targetClass = joinPoint.getTarget().getClass().getName();
		String methodName = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
		Object[] args = joinPoint.getArgs();
		exceptionLogger.error("【异常】class:"+targetClass+",method:"+methodName+",args:"+Arrays.toString(args), e);
	}
}
