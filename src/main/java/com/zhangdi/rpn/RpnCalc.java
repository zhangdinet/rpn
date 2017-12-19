package com.zhangdi.rpn;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import com.google.common.base.Splitter;
import com.zhangdi.rpn.enums.OperatorEnum;
import org.apache.commons.lang3.StringUtils;

/**
 * 逆波兰计算器
 *
 * 操作次数取决于堆内存大小
 * 非线程安全，每个计算器需要独占一个对象
 */
public class RpnCalc {

    //记录计算机当前栈状态
    Deque<BigDecimal> deque;

    //历史操作容器, 非undo操作会记录历史操作容器
    Deque<Deque<BigDecimal>> historyContainer;

    //单行操作历史记录
    Deque<BigDecimal> historyDeque;

    private final String FORMAT_TEMPLATE = "operator %s (position: %s) insucient parameters";

    public RpnCalc(){
        init();
    }

    /**
     * 初始化, 若对计算机操作栈深度和历史记录数有要求, 可考虑借助配置文件通过此方法进行定制
     */
    public void init() {
        deque = new ArrayDeque<>();
        historyContainer = new ArrayDeque<>();
    }

    /**
     * 针对一行输入进行计算
     *
     * @param contents 用户当前输入行
     */
    public Deque<BigDecimal> calc(String contents) {

        //当前位置
        int currentPosition = 1;

        //操作数
        BigDecimal firstNum;
        BigDecimal secondNum;

        historyDeque = new ArrayDeque<>();

        //上一次输入类型
        OperatorEnum lastOperator = OperatorEnum.Unknown;

        List<String> contentList = Splitter.on(" ").trimResults().omitEmptyStrings().splitToList(contents);
        try {
            for (String content : contentList) {
                if (StringUtils.equals(content, " ")) {
                    currentPosition++;
                    continue;
                } else if (StringUtils.equals(content, "+")) {
                    if (deque.size() < 2) {
                        System.out.println(String.format(FORMAT_TEMPLATE, content, currentPosition));
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
                        System.out.println(String.format(FORMAT_TEMPLATE, content, currentPosition));
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
                        System.out.println(String.format(FORMAT_TEMPLATE, content, currentPosition));
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
                        System.out.println(String.format(FORMAT_TEMPLATE, content, currentPosition));
                        break;
                    }
                    if (deque.peekLast().equals(BigDecimal.ZERO)) {
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
                        System.out.println(String.format(FORMAT_TEMPLATE, content, currentPosition));
                        break;
                    } else if (deque.peekLast().doubleValue() < 0) {
                        System.out.println(String.format("operator %s (position: %s) negative error", content, currentPosition));
                        break;
                    }
                    lastOperator = OperatorEnum.Sqrt;
                    firstNum = deque.pollLast();
                    deque.add(BigDecimalMath.sqrt(firstNum));
                    historyDeque.add(firstNum);
                    currentPosition = currentPosition + content.length() + 1;
                } else if (StringUtils.equals(content, "undo")) {
                    if (!lastOperator.equals(OperatorEnum.Clear)) {
                        if (deque.size() > 0) {
                            deque.pollLast();
                        } else {
                            System.out.println(String.format(FORMAT_TEMPLATE, content, currentPosition));
                        }
                    }
                    if (!lastOperator.equals(OperatorEnum.Num) && !lastOperator.equals(OperatorEnum.Undo)) {
                        if (historyContainer.isEmpty()) {
                            continue;
                        }
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
                        System.out.println("input format error, content=" + content);
                        break;
                    }
                    currentPosition = currentPosition + content.length() + 1;
                }
                if (!historyDeque.isEmpty()) {
                    historyContainer.add(historyDeque);
                }
            }
        } catch (Exception e) {
            System.out.println("system error, input new line: ");
        }
        return deque;
    }

    /**
     * 打印计算器当前内容
     *
     * @param deque 存储计算机当前内容的双向队列
     */
    public void printDeque(Deque<BigDecimal> deque) {
        System.out.println(format(deque));
    }

    public String format(Deque<BigDecimal> deque){
        DecimalFormat decimalFormat = new DecimalFormat("#.##########");
        decimalFormat.setRoundingMode(RoundingMode.FLOOR);
        StringBuilder sb = new StringBuilder("stack: ");
        for (BigDecimal num : deque) {
            sb.append(decimalFormat.format(num)).append(" ");
        }
        return sb.toString();
    }

}
