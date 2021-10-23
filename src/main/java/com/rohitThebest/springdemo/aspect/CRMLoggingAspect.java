package com.rohitThebest.springdemo.aspect;

import org.springframework.stereotype.Component;

import java.util.logging.Logger;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
@Component
public class CRMLoggingAspect {

	// setup logger
	private Logger logger = Logger.getLogger(getClass().getName());
	
	// setup pointcut expression
	@Pointcut("execution (* com.rohitThebest.springdemo.controller.*.*(..))")
	private void forControllerPackage() {}
	
	@Pointcut("execution (* com.rohitThebest.springdemo.service.*.*(..))")
	private void forServicePackage() {}
	
	@Pointcut("execution (* com.rohitThebest.springdemo.dao.*.*(..))")
	private void forDAOPackage() {}
	
	@Pointcut("forControllerPackage() || forServicePackage() || forDAOPackage()")
	private void forAppFlow() {}
	
	
	// add @Before annotation
	@Before("forAppFlow()")
	public void before(JoinPoint joinPoint) {
		
		// display method we are calling
		String method = joinPoint.getSignature().toShortString();
		logger.info("=====> in @Before: calling method - " + method);
		
		// display the arguments to the method
		
		Object[] args = joinPoint.getArgs();
		
		for(Object arg : args) {
			
			logger.info("======> argument : " + arg);
		}
	}
	
	// add @AfterReturning advice
	
	@AfterReturning(
			pointcut = "forAppFlow()",
			returning = "result"
			)
	public void afterReturning(JoinPoint joinPoint, Object result) {
		
		// display method we are returning from
		String method = joinPoint.getSignature().toShortString();
		logger.info("=====> in @AfterReturning: from method - " + method);
		
		// display data returned
		logger.info("====> result : " + result);
	}
	
	
}
