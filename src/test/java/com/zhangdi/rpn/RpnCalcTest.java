package com.zhangdi.rpn;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.Deque;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * 逆波兰测试类, 主要测试核心功能即每行运算后栈状态是否和预期结果一致
 */
public class RpnCalcTest {

    RpnCalc rpnCalc;
    Deque<BigDecimal> expectDeque;

    @BeforeMethod
    public void init(){
        rpnCalc = new RpnCalc();
        expectDeque = new ArrayDeque<>();
    }

    /**
     * 边界测试, 暂时不写
     */
    //@Test
    public void test(){

    }

    @Test
    public void testExample1(){
        expectDeque.add(new BigDecimal(5));
        expectDeque.add(new BigDecimal(2));
        String contents = "5 2";
        Assert.assertEquals(rpnCalc.format(rpnCalc.calc(contents)), rpnCalc.format(expectDeque));
    }

    @Test
    public void testExample2(){
        expectDeque.add(new BigDecimal(1.4142135623));
        String contents = "2 sqrt";
        Assert.assertEquals(rpnCalc.format(rpnCalc.calc(contents)), rpnCalc.format(expectDeque));

        contents = "clear 9 sqrt";
        expectDeque.clear();
        expectDeque.add(new BigDecimal(3));
        Assert.assertEquals(rpnCalc.format(rpnCalc.calc(contents)), rpnCalc.format(expectDeque));
    }

   @Test
    public void testExample3(){

        expectDeque.add(new BigDecimal(3));
        String contents = "5 2 -";
        Assert.assertEquals(rpnCalc.format(rpnCalc.calc(contents)), rpnCalc.format(expectDeque));

       contents = "3 -";
       expectDeque.clear();
       expectDeque.add(new BigDecimal(0));
       Assert.assertEquals(rpnCalc.format(rpnCalc.calc(contents)), rpnCalc.format(expectDeque));

       contents = "clear";
       expectDeque.clear();
       Assert.assertEquals(rpnCalc.format(rpnCalc.calc(contents)), rpnCalc.format(expectDeque));
    }

    @Test
    public void testExample4(){
        expectDeque.add(new BigDecimal(5));
        expectDeque.add(new BigDecimal(4));
        expectDeque.add(new BigDecimal(3));
        expectDeque.add(new BigDecimal(2));
        String contents = "5 4 3 2";
        Assert.assertEquals(rpnCalc.format(rpnCalc.calc(contents)), rpnCalc.format(expectDeque));

        contents = "undo undo *";
        expectDeque.clear();
        expectDeque.add(new BigDecimal(20));
        Assert.assertEquals(rpnCalc.format(rpnCalc.calc(contents)), rpnCalc.format(expectDeque));

        contents = "5 *";
        expectDeque.clear();
        expectDeque.add(new BigDecimal(100));
        Assert.assertEquals(rpnCalc.format(rpnCalc.calc(contents)), rpnCalc.format(expectDeque));

        contents = "undo";
        expectDeque.clear();
        expectDeque.add(new BigDecimal(20));
        expectDeque.add(new BigDecimal(5));
        Assert.assertEquals(rpnCalc.format(rpnCalc.calc(contents)), rpnCalc.format(expectDeque));
    }

    @Test
    public void testExample5(){
        expectDeque.add(new BigDecimal(7));
        expectDeque.add(new BigDecimal(6));
        String contents = "7 12 2 /";
        Assert.assertEquals(rpnCalc.format(rpnCalc.calc(contents)), rpnCalc.format(expectDeque));


        contents = "*";
        expectDeque.clear();
        expectDeque.add(new BigDecimal(42));
        Assert.assertEquals(rpnCalc.format(rpnCalc.calc(contents)), rpnCalc.format(expectDeque));

        contents = "4 /";
        expectDeque.clear();
        expectDeque.add(new BigDecimal(10.5));
        Assert.assertEquals(rpnCalc.format(rpnCalc.calc(contents)), rpnCalc.format(expectDeque));
    }

    @Test
    public void testExample6(){
        expectDeque.add(new BigDecimal(1));
        expectDeque.add(new BigDecimal(2));
        expectDeque.add(new BigDecimal(3));
        expectDeque.add(new BigDecimal(4));
        expectDeque.add(new BigDecimal(5));
        String contents = "1 2 3 4 5";
        Assert.assertEquals(rpnCalc.format(rpnCalc.calc(contents)), rpnCalc.format(expectDeque));

        contents = "*";
        expectDeque.clear();
        expectDeque.add(new BigDecimal(1));
        expectDeque.add(new BigDecimal(2));
        expectDeque.add(new BigDecimal(3));
        expectDeque.add(new BigDecimal(20));
        Assert.assertEquals(rpnCalc.format(rpnCalc.calc(contents)), rpnCalc.format(expectDeque));

        contents = "clear 3 4 -";
        expectDeque.clear();
        expectDeque.add(new BigDecimal(-1));
        Assert.assertEquals(rpnCalc.format(rpnCalc.calc(contents)), rpnCalc.format(expectDeque));
    }

    @Test
    public void testExample7(){
        expectDeque.add(new BigDecimal(1));
        expectDeque.add(new BigDecimal(2));
        expectDeque.add(new BigDecimal(3));
        expectDeque.add(new BigDecimal(4));
        expectDeque.add(new BigDecimal(5));
        String contents = "1 2 3 4 5";
        Assert.assertEquals(rpnCalc.format(rpnCalc.calc(contents)), rpnCalc.format(expectDeque));

        expectDeque.clear();
        expectDeque.add(new BigDecimal(120));
        contents = "* * * *";
        Assert.assertEquals(rpnCalc.format(rpnCalc.calc(contents)), rpnCalc.format(expectDeque));
    }

    @Test
    public void testExample8(){
        expectDeque.add(new BigDecimal(11));
        String contents = "1 2 3 * 5 + * * 6 5";
        Assert.assertEquals(rpnCalc.format(rpnCalc.calc(contents)), rpnCalc.format(expectDeque));
    }
}
