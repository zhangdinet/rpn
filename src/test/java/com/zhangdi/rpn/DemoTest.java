package com.zhangdi.rpn;

import java.util.List;

import com.google.common.base.Splitter;
import org.testng.annotations.Test;

public class DemoTest {

    @Test
    public void test(){
        List<String> list = Splitter.on(" ").splitToList("1 2  3 * 5 + *  * 6 5");
        System.out.println(list);
        int a=10;
        a++;
    }

}
