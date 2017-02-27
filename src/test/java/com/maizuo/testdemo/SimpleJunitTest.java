package com.maizuo.testdemo;

import org.junit.*;
import java.util.List;

/**
 * @author gavin
 * @ClassName:SimpleTest
 * @Description:(简单的介绍下单元测试)
 * @Email:gavin@hyx.com
 * @date 2017/1/17 14:39
 */
public class SimpleJunitTest {

    @Ignore
    @Test(expected  = IllegalArgumentException.class)
    public void testExpected1(){
        List list = null;
        list.get(1);
    }

    @Test(timeout  = 1000)
    public void testExpected2(){
        for(int i = 0; i< 100000; i++){
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Ignore
    @Test
    public void testIgnore() {
        System.out.println("@Ignore");
    }



    @After
    public void after() {
        System.out.println("@After");
    }



    @BeforeClass
    public static void beforeClass() {
        System.out.println("@BeforeClass");
    }


    @AfterClass
    public static void afterClass() {
        System.out.println("@AfterClass");
    }
}
