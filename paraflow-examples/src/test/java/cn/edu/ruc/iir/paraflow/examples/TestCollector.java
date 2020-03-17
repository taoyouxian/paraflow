package cn.edu.ruc.iir.paraflow.examples;

import cn.edu.ruc.iir.paraflow.examples.collector.BasicCollector;
import org.testng.annotations.Test;

/**
 * paraflow
 *
 * @author taoyouxian
 */

public class TestCollector
{
    @Test
    public void testMain()
    {
        String[] args = "test tpch 1 320 126 1 1 0 10000000".split(" ");
        BasicCollector.beginCollector(args);
    }
}
