package net.moetang.nekoq.rpc.client.test;

import net.moetang.nekoq.rpc.client.RpcClient;
import net.moetang.nekoq.rpc.core.AppInfo;
import net.moetang.nekoq.rpc.test.IServiceDemo;
import net.moetang.nekoq.rpc.test.RpcObj;
import org.junit.Test;

import java.net.InetSocketAddress;

/**
 * Created by sunhao on 16-3-1.
 */
public class RpcClientTest {
    @Test
    public void testRpcClientUsage() {
        RpcClient<IServiceDemo> rpcClient = RpcClient.create(new InetSocketAddress("127.0.0.1", 14357), IServiceDemo.class);

        IServiceDemo serviceDemo = rpcClient.getInstance();

        serviceDemo.hello(new AppInfo());
        System.out.println(serviceDemo.get(new AppInfo()));
        serviceDemo.sideEffect(new RpcObj("sideEffect client", 87685), new AppInfo());
        System.out.println(serviceDemo.cal(new RpcObj("cal what?", 10445), new AppInfo()));
        serviceDemo.intMethod(88, new AppInfo());
        System.out.println(serviceDemo.ping(new AppInfo()));
    }
}
