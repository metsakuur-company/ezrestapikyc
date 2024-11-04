package com.metsakuur.ezway.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Aspect
@Slf4j
public class LoggingAspect {

    @AfterReturning(value = "execution(* com.metsakuur.ezway.service..*(..))" , returning = "result")
    public void afterReturningEzCallService(JoinPoint  joinPoint , Object result) {
        log.debug("Log before executing method...");
        log.debug("This is qualified name: {}", joinPoint.getSignature().getDeclaringTypeName());
        log.debug("This is method name: {}", joinPoint.getSignature().getName());
        log.debug("Arguments of method: {}", Arrays.toString(joinPoint.getArgs()));
    }

}
