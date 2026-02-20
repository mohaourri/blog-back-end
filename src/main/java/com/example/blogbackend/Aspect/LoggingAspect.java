package com.example.blogbackend.Aspect;



import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LoggingAspect {


    @Pointcut("execution(* com.example.blogbackend.Service.Impl.*.*(..))")
    public void serviceMethods() {}

    @Pointcut("execution(* com.example.blogbackend.Controller.*.*(..))")
    public void controllerMethods() {}


    @Before("serviceMethods()")
    public void logBefore(JoinPoint joinPoint) {
        log.info("→ [SERVICE] {}.{}() | args: {}",
                joinPoint.getTarget().getClass().getSimpleName(),
                joinPoint.getSignature().getName(),
                Arrays.toString(joinPoint.getArgs()));
    }


    @AfterReturning(pointcut = "serviceMethods()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        log.info("← [SERVICE] {}.{}() | résultat: {}",
                joinPoint.getTarget().getClass().getSimpleName(),
                joinPoint.getSignature().getName(),
                result);
    }


    @AfterThrowing(pointcut = "serviceMethods()", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        log.error("✗ [SERVICE] {}.{}() | exception: {}",
                joinPoint.getTarget().getClass().getSimpleName(),
                joinPoint.getSignature().getName(),
                exception.getMessage());
    }


    @Before("controllerMethods()")
    public void logController(JoinPoint joinPoint) {
        log.info("⇒ [CONTROLLER] {}.{}()",
                joinPoint.getTarget().getClass().getSimpleName(),
                joinPoint.getSignature().getName());
    }
}