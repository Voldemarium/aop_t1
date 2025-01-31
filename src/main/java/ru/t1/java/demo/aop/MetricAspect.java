package ru.t1.java.demo.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.aop.annotations.Metric;
import ru.t1.java.demo.kafka.KafkaErrorProducer;
import ru.t1.java.demo.kafka.KafkaTimeExceededProducer;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;

@Async
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class MetricAspect {
    private final KafkaTimeExceededProducer kafkaTimeExceededProducer;
    @Value("${t1.kafka.topic.metrics}")
    private String metricsTopic;

    @Around("@annotation(ru.t1.java.demo.aop.annotations.Metric)")
    public Object logExecTime(ProceedingJoinPoint pJoinPoint) throws Throwable {
        log.info("Вызов метода: {}", pJoinPoint.getSignature().getName());

        long beforeTime = System.currentTimeMillis();
        Object result;
        try {
            result = pJoinPoint.proceed();
        } finally {
            long maxExecutionTime = ((MethodSignature) pJoinPoint.getSignature()).getMethod()
                    .getAnnotation(Metric.class).maxExecutionTime();
            long executionTime = System.currentTimeMillis() - beforeTime;
            log.info("Время исполнения: {} ms", executionTime);
            if (executionTime > maxExecutionTime) {
                String methodName = pJoinPoint.getSignature().getName();
                Object[] parameters = pJoinPoint.getArgs();
                String message = "Время исполнения " + executionTime + "ms, " +
                        "метод " + methodName +
                        "с параметрами: " + Arrays.toString(parameters);
                kafkaTimeExceededProducer.sendOverMaxExecutionTimeMessage(metricsTopic, message);
                log.info("Максимальное время {}ms превышено", maxExecutionTime);
            }
        }
        return result;
    }

}
