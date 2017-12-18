package com.zhangdi.rpn;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Scanner;

import com.google.common.base.Splitter;
import org.apache.commons.lang3.StringUtils;

public class Bootstrap {

    public static void main(String[] args) {

        //记录计算机当前栈状态
        Deque<BigDecimal> deque = new ArrayDeque<>();

        //历史操作容器, 非undo操作会记录历史操作容器
        Deque<Deque<BigDecimal>> historyContainer = new ArrayDeque<>();
        BigDecimal firstNum;
        BigDecimal secondNum;
        while (true) {
            Scanner scanner = new Scanner(System.in);
            String contents = scanner.nextLine();
            List<String> contentList = Splitter.on(" ").trimResults().omitEmptyStrings().splitToList(contents);
            Deque<BigDecimal> historyDeque = new ArrayDeque<>();
            Boolean undoFlag = false;
            for (String content : contentList) {
                if (StringUtils.equals(content, "+")) {
                    undoFlag = false;
                    secondNum = deque.pollLast();
                    firstNum = deque.pollLast();
                    deque.add(firstNum.add(secondNum));
                    historyDeque.add(firstNum);
                    historyDeque.add(secondNum);
                } else if (StringUtils.equals(content, "-")) {
                    undoFlag = false;
                    secondNum = deque.pollLast();
                    firstNum = deque.pollLast();
                    deque.add(firstNum.subtract(secondNum));
                    historyDeque.add(firstNum);
                    historyDeque.add(secondNum);
                } else if (StringUtils.equals(content, "*")) {
                    undoFlag = false;
                    secondNum = deque.pollLast();
                    firstNum = deque.pollLast();
                    deque.add(firstNum.multiply(secondNum));
                    historyDeque.add(firstNum);
                    historyDeque.add(secondNum);
                } else if (StringUtils.equals(content, "/")) {
                    undoFlag = false;
                    secondNum = deque.pollLast();
                    firstNum = deque.pollLast();
                    deque.add(firstNum.multiply(secondNum));
                    historyDeque.add(firstNum);
                    historyDeque.add(secondNum);
                } else if (StringUtils.equals(content, "sqrt")) {
                    undoFlag = false;
                    firstNum = deque.pollLast();
                    deque.add(new BigDecimal(Math.sqrt(firstNum.doubleValue())));
                    historyDeque.add(firstNum);
                } else if (StringUtils.equals(content, "undo")) {
                    if(!undoFlag){
                        deque.pollLast();
                    }
                    undoFlag = true;
                    for (BigDecimal historyNum : historyContainer.pollLast()) {
                        deque.add(historyNum);
                    }
                } else if (StringUtils.equals(content, "clear")) {
                    undoFlag = false;
                    for (BigDecimal num : deque) {
                        historyDeque.add(num);
                    }
                    deque.clear();
                } else {
                    undoFlag = false;
                    deque.add(new BigDecimal(content));

                }
                if (!historyDeque.isEmpty()) {
                    historyContainer.add(historyDeque);
                }
            }
            printDeque(deque);
        }
    }

    public static void printDeque(Deque<BigDecimal> deque){
        DecimalFormat decimalFormat = new DecimalFormat("#.##########");
        StringBuilder sb = new StringBuilder("stack: ");
        for(BigDecimal num:deque){
            sb.append(decimalFormat.format(num)).append(" ");
        }
        System.out.println(sb.toString());
    }
}
