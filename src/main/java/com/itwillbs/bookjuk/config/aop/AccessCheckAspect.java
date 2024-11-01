package com.itwillbs.bookjuk.config.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
@Slf4j
public class AccessCheckAspect {

    @Before("com.itwillbs.bookjuk.config.aop.JoinPointConfig.statisticsControllerPointcut()" +
            "|| com.itwillbs.bookjuk.config.aop.JoinPointConfig.statisticsServicePointcut()")
    public void beforeBoardController(JoinPoint joinPoint) {
        log.info("before {}", joinPoint);
    }
}
