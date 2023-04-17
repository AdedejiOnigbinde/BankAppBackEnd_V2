// package com.base.BaseDependencies.Aspects;

// import java.util.Arrays;

// import org.apache.logging.log4j.LogManager;
// import org.apache.logging.log4j.Logger;
// import org.aspectj.lang.JoinPoint;
// import org.aspectj.lang.annotation.AfterReturning;
// import org.aspectj.lang.annotation.AfterThrowing;
// import org.aspectj.lang.annotation.Aspect;
// import org.aspectj.lang.annotation.Before;
// import org.aspectj.lang.annotation.Pointcut;
// import org.springframework.stereotype.Component;

// @Component
// @Aspect
// public class LoggingAspect {

//     private static final Logger logger = LogManager.getLogger(LoggingAspect.class);
    
//     @Pointcut("within(com.base.BaseDependencies..*)")
//     public void generalPointCutLogger() {}
    
//     @Before("generalPointCutLogger()")
//     public void logMethodInitialization(JoinPoint jp) {
//         String methodArguments = Arrays.toString(jp.getArgs());
//         logger.info("{} successfully invoked with arguments {}", getClassAndMethodName(jp), methodArguments);
//     }
    
//     @AfterReturning(pointcut = "generalPointCutLogger()", returning = "response")
//     public void logMethodResponse(JoinPoint jp, Object response) {
//         logger.info("{} successfully returned with response {}", getClassAndMethodName(jp), response);
//     }
    
//     @AfterThrowing(pointcut = "generalPointCutLogger()", throwing = "exception")
//     public void logMethodException(JoinPoint jp, Throwable exception) {
//         String exceptionName = exception.getClass().getName();
//         logger.error("{} thrown in {} with message: {}", exceptionName, getClassAndMethodName(jp),
//                 exception.getMessage());
//     }
    
//     public String getClassAndMethodName(JoinPoint jp) {
//         return String.format("%s -> %s", jp.getTarget().getClass(), jp.getSignature().getName());
//     }
    

// }
