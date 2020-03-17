package cn.edu.ruc.iir.paraflow.examples;

import cn.edu.ruc.iir.paraflow.examples.collector.BasicCollector;
import cn.edu.ruc.iir.paraflow.examples.loader.BasicLoader;
import org.testng.annotations.Test;

/**
 * paraflow
 *
 * @author taoyouxian
 */

public class TestLoader
{
    @Test
    public void testMain()
    {
        String[] args = "test tpch 0 319".split(" ");
        BasicLoader.beginLoader(args);
    }
}
