package com.maizuo.testdemo;

import org.junit.Test;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author gavin
 * @ClassName:SimpleMockitoTest
 * @Description:(简单的介绍下Mockito使用)
 * @Email:gavin@hyx.com
 * @date 2017/1/17 14:41
 */
public class SimpleMockitoTest {
    @Test
    public void testMock(){
        //创建mock对象，参数可以是类，也可以是接口
        List<String> list = mock(List.class);
        //设置方法的预期返回值
        when(list.get(0)).thenReturn("helloworld");
        String result = list.get(0);
        //验证方法调用(是否调用了get(0))
        verify(list).get(0);
        //使用断言测试
        assertEquals("oworld", result);

        /** 注意: 创建mock对象不能对final，Anonymous ，primitive类进行mock */
    }

    @Test
    public void when_thenReturn(){
        //mock一个Iterator类
        Iterator iterator = mock(Iterator.class);
        //预设当iterator调用next()时第一次返回hello，第二次返回world，第n次都返回wo
        when(iterator.next()).thenReturn("hello").thenReturn("world").thenReturn("wo");
        //使用mock的对象
        String result = iterator.next() + " " + iterator.next() + " " + iterator.next();
        //验证结果
        assertEquals("hello world world",result);
    }

    //预期应该抛出 IOException
    @Test(expected = IOException.class)
    public void when_thenThrow() throws IOException {
        OutputStream outputStream = mock(OutputStream.class);
        OutputStreamWriter writer = new OutputStreamWriter(outputStream);
        //预设当流关闭时抛出异常
        doThrow(new IOException()).when(outputStream).close();
        outputStream.close();
    }
}
