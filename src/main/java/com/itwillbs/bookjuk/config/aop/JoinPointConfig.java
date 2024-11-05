package com.itwillbs.bookjuk.config.aop;

import org.aspectj.lang.annotation.Pointcut;

public class JoinPointConfig {

    @Pointcut("execution(* com.itwillbs.bookjuk.controller.statistics..*(..))")
    public void statisticsControllerPointcut() {}

    @Pointcut("execution(* com.itwillbs.bookjuk.service.statistics..*(..))")
    public void statisticsServicePointcut() {}



    //to intercept method calls for both layers:
//    @Pointcut("io..springaop..aspect.JoinPointConfig.dataLayerPointcut() || "
//            + "io..springaop..aspect.JoinPointConfig.businessLayerPointcut()")
//    public void allLayersPointcut() {}
//
//    //for a particular bean
//    @Pointcut("bean(movie*)")
//    public void movieBeanPointcut() {}
//
//    //custom aspect annotation
//    @Pointcut("@annotation(io..springaop..aspect.MeasureTime)")
//    public void measureTimeAnnotation() {}

}
