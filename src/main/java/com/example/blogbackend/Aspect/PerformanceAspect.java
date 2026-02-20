package com.example.blogbackend.Aspect;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class PerformanceAspect {

    @Around("execution(* com.example.blogbackend.Service.Impl.*.*(..))")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        Object result = joinPoint.proceed(); // exécute la méthode réelle

        long duration = System.currentTimeMillis() - start;

        if (duration > 500) {
            log.warn("⚠ [PERF] {}.{}() LENT → {} ms",
                    joinPoint.getTarget().getClass().getSimpleName(),
                    joinPoint.getSignature().getName(),
                    duration);
        } else {
            log.info("⏱ [PERF] {}.{}() → {} ms",
                    joinPoint.getTarget().getClass().getSimpleName(),
                    joinPoint.getSignature().getName(),
                    duration);
        }

        return result;
    }
}
