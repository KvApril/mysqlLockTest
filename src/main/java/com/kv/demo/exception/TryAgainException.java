package com.kv.demo.exception;

/**
 * 更新重试异常
 */
public class TryAgainException extends ApiException {

    public TryAgainException(ApiResultEnum apiResultEnum) {
        super(apiResultEnum);
    }
}