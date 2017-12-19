package com.zhangdi.rpn;

import java.util.Scanner;

/**
 * 启动类
 */
public class Bootstrap {

    public static void main(String[] args) {
        RpnCalc rpnCalc = new RpnCalc();
        while (true) {
            Scanner scanner = new Scanner(System.in);
            String contents = scanner.nextLine();
            rpnCalc.printDeque(rpnCalc.calc(contents));
        }
    }
}
