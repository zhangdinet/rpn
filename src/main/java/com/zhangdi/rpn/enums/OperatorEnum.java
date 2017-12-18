package com.zhangdi.rpn.enums;

/**
 * 逆波兰操作符枚举
 */
public enum OperatorEnum {

    Add("+"),
    Subtract("-"),
    Multiply("*"),
    Devide("/"),
    Sqrt("sqrt"),
    Undo("undo"),
    Clear("clear"),
    Num("num"),
    Unknown("Unknown");

    private String value;

    OperatorEnum(String value){
        this.value = value;
    }
}
