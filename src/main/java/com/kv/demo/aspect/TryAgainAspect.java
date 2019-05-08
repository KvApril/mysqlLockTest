package com.kv.demo.aspect;

import com.kv.demo.exception.ApiException;
import com.kv.demo.exception.ApiResultEnum;
import com.kv.demo.exception.TryAgainException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Aspect
@Configuration
@Component
public class TryAgainAspect implements Ordered {
    private static final int DEFAULT_MAX_RETRIES = 3;

    private int maxRetries = DEFAULT_MAX_RETRIES;
    private int order = 1;

    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    public int getOrder() {
        return this.order;
    }

    @Pointcut("@annotation(com.kv.demo.annotation.IsTryAgain)")
    public void retryOnOptFailure() {
    }

    @Around("retryOnOptFailure()")
    @Transactional(rollbackFor = Exception.class)
    public Object doConcurrentOperation(ProceedingJoinPoint pjp) throws Throwable {
        int numAttempts = 0;
        do {
            numAttempts++;
            try {
                //再次执行业务代码
                return pjp.proceed();
            } catch (TryAgainException ex) {
                if (numAttempts > maxRetries) {
                    //如果大于 默认的重试机制 次数，我们这回就真正的抛出去了
                    throw new ApiException(ApiResultEnum.ERROR_TRY_AGAIN_FAILED);
                }else{
                    //如果 没达到最大的重试次数，将再次执行
                    System.out.println("=====正在重试====="+numAttempts+"次");
                }
            }
        }while (numAttempts <= maxRetries);

        return null;
    }
}
