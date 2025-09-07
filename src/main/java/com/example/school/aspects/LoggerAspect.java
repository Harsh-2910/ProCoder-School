package com.example.school.aspects;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Slf4j
@Aspect
@Component
public class LoggerAspect {
    @Around("execution(* com.example.school..*.*(..))")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable{
        log.info(joinPoint.getSignature().toString()+" method execution started");
        Instant start = Instant.now();
        Object returnObj = joinPoint.proceed();
        Instant end = Instant.now();
        long timeElapsed = Duration.between(start,end).toMillis();
        log.info("time taken by " + joinPoint.getSignature().toString()+" to execute is " + timeElapsed + " ms");
        log.info(joinPoint.getSignature().toString()+" method execution finished");
        return returnObj;
    }

    @AfterThrowing(value = "execution(* com.example.school..*.*(..))", throwing = "ex")
    public void logException(JoinPoint joinPoint, Exception ex) throws Throwable{
        log.error(joinPoint.getSignature().toString()+ " exception occured due to " + ex.getMessage());
    }
}
