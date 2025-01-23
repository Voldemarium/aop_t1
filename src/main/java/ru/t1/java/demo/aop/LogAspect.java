package ru.t1.java.demo.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.model.Client;

import java.util.List;

import static java.util.Objects.isNull;

@Slf4j
@Aspect
@Component
@Order(0)
public class LogAspect {

    @Pointcut("execution(* ru.t1.java.demo.service.*.*(..))")
        public void loggingMethods() {

    }

//    @Before("loggingMethods()")
    @Before("@annotation(ru.t1.java.demo.aop.annotations.LogExecution)")
    @Order(1)
    public void logAnnotationBefore(JoinPoint joinPoint) {
        log.info("ASPECT BEFORE ANNOTATION: Call method: {}", joinPoint.getSignature().getName());
    }

    @AfterThrowing(pointcut = "@annotation(ru.t1.java.demo.aop.annotations.LogException)")
    @Order(0)
    public void logExceptionAnnotation(JoinPoint joinPoint) {
        System.err.println("ASPECT EXCEPTION ANNOTATION: Logging exception: {}" + joinPoint.getSignature().getName());
    }

    @AfterReturning(
            pointcut = "@annotation(ru.t1.java.demo.aop.annotations.HandlingResult)",
            returning = "result")
    public void handleResult(JoinPoint joinPoint, List<Object> result) {
        log.info("В результате выполнения метода {}", joinPoint.getSignature().toShortString());
        log.info("получен результат: {} ", result);
        log.info("Подробности: \n");
        List<Object> result_ = isNull(result) ? List.of() : result;
        log.info(result_.toString());
    }

}
