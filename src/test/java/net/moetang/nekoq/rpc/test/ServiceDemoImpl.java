package net.moetang.nekoq.rpc.test;

import net.moetang.nekoq.rpc.core.AppInfo;

/**
 * Created by sunhao on 16-3-1.
 */
public class ServiceDemoImpl implements IServiceDemo {
    public void hello(AppInfo appInfo) {
        System.out.println("hello world.");
    }

    public RpcObj get(AppInfo appInfo) {
        return new RpcObj("get method", 445);
    }

    public void sideEffect(RpcObj rpcObj, AppInfo appInfo) {
        System.out.println(rpcObj);
    }

    public RpcObj cal(RpcObj rpcObj, AppInfo appInfo) {
        return new RpcObj(rpcObj.toString(), 98764567);
    }

    public void intMethod(int aaa, AppInfo appInfo) {
        System.out.println("int method :: " + aaa);
    }

    public Pong ping(AppInfo appInfo) {
        Pong pong = new Pong();
        pong.setMsg("pong");
        return pong;
    }
}
