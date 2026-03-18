package com.example.mall.common;

/**
 * 业务响应码枚举
 */
public enum ResultCode {

    // 成功
    SUCCESS(200, "操作成功"),

    // 客户端错误 4xx
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "资源不存在"),
    METHOD_NOT_ALLOWED(405, "请求方法不允许"),

    // 业务错误 1000-1999
    PRODUCT_NOT_FOUND(1001, "商品不存在"),
    PRODUCT_STOCK_INSUFFICIENT(1002, "商品库存不足"),
    PRODUCT_ALREADY_EXISTS(1003, "商品已存在"),

    ORDER_NOT_FOUND(1101, "订单不存在"),
    ORDER_ALREADY_PAID(1102, "订单已支付"),
    ORDER_CANCEL_FAILED(1103, "订单取消失败"),
    ORDER_STATUS_ERROR(1104, "订单状态异常"),

    // 服务器错误 5xx
    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),
    SERVICE_UNAVAILABLE(503, "服务暂不可用");

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
