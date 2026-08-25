package com.tachibana.projectsekai05.common.enums;

/**
 * 统一响应码
 */
public enum ResultCode {

    /** 成功 */
    SUCCESS(200, "操作成功"),

    /** 请求参数错误 */
    BAD_REQUEST(400, "请求参数错误"),

    /** 未认证 */
    UNAUTHORIZED(401, "未登录或登录已过期"),

    /** 无权限 */
    FORBIDDEN(403, "没有权限访问"),

    /** 资源不存在 */
    NOT_FOUND(404, "请求的资源不存在"),

    /** 服务端错误 */
    ERROR(500, "系统内部错误"),

    /** 业务失败 */
    FAIL(600, "业务处理失败");

    private final int code;
    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
