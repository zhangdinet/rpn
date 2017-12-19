package com.zhangdi.rpn;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.Deque;

import com.zhangdi.rpn.enums.OperatorEnum;

public class RpnCalc {


    //记录计算机当前栈状态
    Deque<BigDecimal> deque = new ArrayDeque<>();

    //历史操作容器, 非undo操作会记录历史操作容器
    Deque<Deque<BigDecimal>> historyContainer = new ArrayDeque<>();

    //操作数
    BigDecimal firstNum;
    BigDecimal secondNum;

    //上一次输入类型
    OperatorEnum lastOperator = OperatorEnum.Unknown;

    public static void init(){

    }


}
