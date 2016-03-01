package net.moetang.nekoq.rpc.server.test;

import net.moetang.nekoq.rpc.server.RpcServer;
import net.moetang.nekoq.rpc.test.IServiceDemo;
import net.moetang.nekoq.rpc.test.ServiceDemoImpl;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * Created by sunhao on 16-3-1.
 */
public class RpcServerTest {
    @Test
    public void testServerUsage() throws InterruptedException {
        IServiceDemo serviceDemo = new ServiceDemoImpl();

        RpcServer.create(new InetSocketAddress("127.0.0.1", 14357), serviceDemo, IServiceDemo.class);

        TimeUnit.SECONDS.sleep(10000000);
    }
}
