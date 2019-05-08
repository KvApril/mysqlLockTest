package com.kv.demo.exception;

public enum ApiResultEnum {
    SUCCESS(200,"ok"),
    FAILED(400,"请求失败"),
    ERROR(500,"服务器错误"),
    ERROR_NULL(501,"空指针异常"),
    ERROR_CLASS_CAST(502,"类型转换异常"),
    ERROR_RUNTION(503,"运行时异常"),
    ERROR_MOTHODNOTSUPPORT(505,"请求方法错误"),
    ERROR_TRY_AGAIN(506,"正在重试"),
    ERROR_TRY_AGAIN_FAILED(507,"重试失败");
    private int status;
    private String message;

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }
    private ApiResultEnum(int status,String message) {
        this.status = status;
        this.message = message;
    }


}