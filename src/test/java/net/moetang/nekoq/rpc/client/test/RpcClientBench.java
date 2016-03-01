package net.moetang.nekoq.rpc.client.test;

import net.moetang.nekoq.rpc.client.RpcClient;
import net.moetang.nekoq.rpc.core.AppInfo;
import net.moetang.nekoq.rpc.test.IServiceDemo;

import java.net.InetSocketAddress;

/**
 * Created by sunhao on 16-3-2.
 */
public class RpcClientBench {
    public static void main(String[] args) {
        RpcClient<IServiceDemo> rpcClient = RpcClient.create(new InetSocketAddress("127.0.0.1", 14357), IServiceDemo.class);

        IServiceDemo serviceDemo = rpcClient.getInstance();

        runTest(serviceDemo);

        runTest(serviceDemo);

        runTest(serviceDemo);
    }

    private static void runTest(IServiceDemo serviceDemo) {
        final int CNT = 200000;

        long start = System.currentTimeMillis();
        for (int i = 0; i < CNT; i++) {
            bench(serviceDemo);
        }
        long end = System.currentTimeMillis();

        long time = end - start;
        double count = (double) CNT;

        System.out.println("time: " + time + " , qps: " + (count / time * 1000));
    }

    private static void bench(IServiceDemo serviceDemo) {
        serviceDemo.ping(new AppInfo());
    }
}
