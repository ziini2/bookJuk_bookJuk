package com.itwillbs.bookjuk.config.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect  // Aspect로 선언
@Component  // 스프링 빈으로 등록
public class LoggingAspect {

    final String STATISTICSPOINTCUT = "com.itwillbs.bookjuk.config.aop.JoinPointConfig.statisticsControllerPointcut() || com.itwillbs.bookjuk.config.aop.JoinPointConfig.statisticsServicePointcut()";

    @AfterReturning(value = STATISTICSPOINTCUT,
            returning = "result")
    //@AfterReturning("execution(* io.datajek.springaop.movierecommenderaop.data.*.*(..))")
    public void logAfterExecution(JoinPoint joinPoint, Object result) {
        //logger.info("Method {} complete", joinPoint);
        log.info("{} returned with: {}", joinPoint, result);
    }

    @AfterThrowing(value = STATISTICSPOINTCUT,
            throwing = "exception")
    public void logAfterException(JoinPoint joinPoint, Object exception) {
        log.info("Exception in {} returned with: {}", joinPoint, exception);
    }

    @After(value = STATISTICSPOINTCUT)
    public void logAfterMethod(JoinPoint joinPoint) {
        log.info("After {}", joinPoint);
    }


}
