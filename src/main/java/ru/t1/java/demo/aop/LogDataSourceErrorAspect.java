package ru.t1.java.demo.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.exception.DataSourceErrorException;
import ru.t1.java.demo.model.DataSourceErrorLog;
import ru.t1.java.demo.service.DataSourceErrorLogService;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LogDataSourceErrorAspect {
    private final DataSourceErrorLogService dataSourceErrorLogService;

    @AfterThrowing(pointcut = "@annotation(ru.t1.java.demo.aop.annotations.LogDataSourceError)", throwing = "e")
    public void logExceptionAnnotation(JoinPoint joinPoint, Exception e) {
        System.err.println("ASPECT EXCEPTION ANNOTATION: Logging exception: {}" + joinPoint.getSignature().getName());
        String signatureName = joinPoint.getSignature().toShortString();

        DataSourceErrorLog errorLog = DataSourceErrorLog.builder()
                .methodSignature(signatureName)
                .stackTrace(Arrays.toString(e.getStackTrace()))
                .message(e.getMessage())
                .build();
        e.printStackTrace();

        String methodName = joinPoint.getSignature().getName();
        Class<?>[] userInterfaces = AopProxyUtils.proxiedUserInterfaces(joinPoint.getTarget());
        boolean isDataSourceErrorLogRepository = Arrays.stream(userInterfaces).map(Class::getSimpleName)
                .anyMatch(s -> s.equals("DataSourceErrorLogRepository"));
        if (!(isDataSourceErrorLogRepository && methodName.equals("save"))) {
           if (!dataSourceErrorLogService.sendDataSourceErrorLog("t1_demo_metrics", errorLog)) {
               log.error("Failed to send message: {}", errorLog);
               dataSourceErrorLogService.saveDataSourceErrorLog(errorLog);
               log.error("saved message to database: {}", errorLog);
           }
        } else {
            log.error("THIS IS ERROR RECORDING ERROR!!!");
        }
    }
}
