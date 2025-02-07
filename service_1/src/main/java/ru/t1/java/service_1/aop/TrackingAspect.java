package ru.t1.java.service_1.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

@Async
@Slf4j
@Aspect
@Component
public class TrackingAspect {

    private static final AtomicLong START_TIME = new AtomicLong();

    @Before("@annotation(ru.t1.java.demo.aop.annotations.Track)")
    public void logExecTime(JoinPoint joinPoint) {
        log.info("Старт метода: {}", joinPoint.getSignature().toShortString());
        START_TIME.addAndGet(System.currentTimeMillis());
    }

    @Around("@annotation(ru.t1.java.demo.aop.annotations.Track)")
    public Object logExecTime(ProceedingJoinPoint pJoinPoint) throws Throwable {
        log.info("Вызов метода: {}", pJoinPoint.getSignature().toShortString());
        long beforeTime = System.currentTimeMillis();
        Object result = null;
        try {
            result = pJoinPoint.proceed();//Important
            long afterTime = System.currentTimeMillis();
            log.info("Время исполнения: {} ms", (afterTime - beforeTime));
        } catch (Throwable throwable) {
            log.error(throwable.getMessage());
        }
        return result;
    }

}
