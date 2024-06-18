package com.base.BaseDependencies.Aspects;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
@Aspect
public class LoggingAspect {
    
    @Pointcut("within(com.base.BaseDependencies.*)")
    public void generalPointCutLogger() {}
    
    @Before("generalPointCutLogger()")
    public void logMethodInitialization(JoinPoint jp) {
        String methodArguments = Arrays.toString(jp.getArgs());
        log.info("{} successfully invoked with arguments {}", getClassAndMethodName(jp), methodArguments);
    }
    
    @AfterReturning(pointcut = "generalPointCutLogger()", returning = "response")
    public void logMethodResponse(JoinPoint jp, Object response) {
        log.info("{} successfully returned with response {}", getClassAndMethodName(jp), response);
    }
    
    @AfterThrowing(pointcut = "generalPointCutLogger()", throwing = "exception")
    public void logMethodException(JoinPoint jp, Throwable exception) {
        String exceptionName = exception.getClass().getName();
        log.error("{} thrown in {} with message: {}", exceptionName, getClassAndMethodName(jp),
                exception.getMessage());
    }
    
    public String getClassAndMethodName(JoinPoint jp) {
        return String.format("%s -> %s", jp.getTarget().getClass(), jp.getSignature().getName());
    }
    

}
