package com.offcn.enums;

public enum  OrderStatusEnum {

    UNPAY(0,"未支付"),
    CANCEL(1,"已取消"),
    PAYED(2,"支付成功"),
    WAITING(3,"等待发货"),
    SENDED(4,"已送达"),
    SUCCESS(5,"交易完成"),
    FAIL(6,"交易未完成");


    private Integer code;

    private String status;

    private OrderStatusEnum(Integer code ,String status){
        this.code  = code;
        this.status = status;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
