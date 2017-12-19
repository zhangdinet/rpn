package com.zhangdi.rpn;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Scanner;

import com.google.common.base.Splitter;
import com.zhangdi.rpn.enums.OperatorEnum;
import org.apache.commons.lang3.StringUtils;

public class Bootstrap {

    public static void main(String[] args) {

        //记录计算机当前栈状态
        Deque<BigDecimal> deque = new ArrayDeque<>();

        //历史操作容器, 非undo操作会记录历史操作容器
        Deque<Deque<BigDecimal>> historyContainer = new ArrayDeque<>();

        //操作数
        BigDecimal firstNum;
        BigDecimal secondNum;

        //上一次输入类型
        OperatorEnum lastOperator = OperatorEnum.Unknown;

        while (true) {
            Scanner scanner = new Scanner(System.in);
            String contents = scanner.nextLine();
            List<String> contentList = Splitter.on(" ").trimResults().omitEmptyStrings().splitToList(contents);
            Deque<BigDecimal> historyDeque = new ArrayDeque<>();
            int currentPosition = 1;
            try {
                for (String content : contentList) {
                    if(StringUtils.equals(content, " ")){
                        currentPosition++;
                        continue;
                    }else if (StringUtils.equals(content, "+")) {
                        if (deque.size() < 2) {
                            System.out.println(String.format("operator %s (position: %s) insucient parameters", content, currentPosition));
                            break;
                        }
                        lastOperator = OperatorEnum.Add;
                        secondNum = deque.pollLast();
                        firstNum = deque.pollLast();
                        deque.add(firstNum.add(secondNum));
                        historyDeque.add(firstNum);
                        historyDeque.add(secondNum);
                        currentPosition = currentPosition + content.length() + 1;
                    } else if (StringUtils.equals(content, "-")) {
                        if (deque.size() < 2) {
                            System.out.println(String.format("operator %s (position: %s) insucient parameters", content, currentPosition));
                            break;
                        }
                        lastOperator = OperatorEnum.Subtract;
                        secondNum = deque.pollLast();
                        firstNum = deque.pollLast();
                        deque.add(firstNum.subtract(secondNum));
                        historyDeque.add(firstNum);
                        historyDeque.add(secondNum);
                        currentPosition = currentPosition + content.length() + 1;
                    } else if (StringUtils.equals(content, "*")) {
                        if (deque.size() < 2) {
                            System.out.println(String.format("operator %s (position: %s) insucient parameters", content, currentPosition));
                            break;
                        }
                        lastOperator = OperatorEnum.Multiply;
                        secondNum = deque.pollLast();
                        firstNum = deque.pollLast();
                        deque.add(firstNum.multiply(secondNum));
                        historyDeque.add(firstNum);
                        historyDeque.add(secondNum);
                        currentPosition = currentPosition + content.length() + 1;
                    } else if (StringUtils.equals(content, "/")) {
                        if (deque.size() < 2) {
                            System.out.println(String.format("operator %s (position: %s) insucient parameters", content, currentPosition));
                            break;
                        }
                        if (deque.pollLast().equals(BigDecimal.ZERO)) {
                            System.out.println("operator / divide 0 error");
                            break;
                        }
                        lastOperator = OperatorEnum.Devide;
                        secondNum = deque.pollLast();
                        firstNum = deque.pollLast();
                        deque.add(firstNum.divide(secondNum));
                        historyDeque.add(firstNum);
                        historyDeque.add(secondNum);
                        currentPosition = currentPosition + content.length() + 1;
                    } else if (StringUtils.equals(content, "sqrt")) {
                        if (deque.size() < 1) {
                            System.out.println("operator sqrt (positio:) insucient parameters");
                            break;
                        } else if (deque.peekLast().doubleValue() < 0) {
                            System.out.println(String.format("operator %s (position: %s) insucient parameters", content, currentPosition));
                            break;
                        }
                        lastOperator = OperatorEnum.Sqrt;
                        firstNum = deque.pollLast();
                        deque.add(BigDecimalMath.sqrt(firstNum));
                        historyDeque.add(firstNum);
                        currentPosition = currentPosition + content.length() + 1;
                    } else if (StringUtils.equals(content, "undo")) {
                        if (!lastOperator.equals(OperatorEnum.Clear)) {
                            deque.pollLast();
                        }
                        if (!lastOperator.equals(OperatorEnum.Num) && !lastOperator.equals(OperatorEnum.Undo)) {
                            for (BigDecimal historyNum : historyContainer.pollLast()) {
                                deque.add(historyNum);
                            }
                        }
                        currentPosition = currentPosition + content.length() + 1;
                    } else if (StringUtils.equals(content, "clear")) {
                        for (BigDecimal num : deque) {
                            historyDeque.add(num);
                        }
                        deque.clear();
                        lastOperator = OperatorEnum.Clear;
                        currentPosition = currentPosition + content.length() + 1;
                    } else {
                        try {
                            deque.add(new BigDecimal(content));
                            lastOperator = OperatorEnum.Num;
                        } catch (Exception e) {
                            System.out.println("输入内容格式有误, content=" + content);
                            break;
                        }
                    }
                    if (!historyDeque.isEmpty()) {
                        historyContainer.add(historyDeque);
                    }
                }
            } catch (Exception e) {
                System.out.println("系统错误,重新输入");
            }
            printDeque(deque);
        }
    }

    /**
     * 打印计算器当前内容
     *
     * @param deque 存储计算机当前内容的双向队列
     */
    public static void printDeque(Deque<BigDecimal> deque) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##########");
        StringBuilder sb = new StringBuilder("stack: ");
        for (BigDecimal num : deque) {
            sb.append(decimalFormat.format(num)).append(" ");
        }
        System.out.println(sb.toString());
    }
}
