package net.moetang.nekoq.rpc.test;

import net.moetang.nekoq.rpc.core.AppInfo;

/**
 * Created by sunhao on 16-3-1.
 */
public interface IServiceDemo {
    void hello(AppInfo appInfo);

    RpcObj get(AppInfo appInfo);

    void sideEffect(RpcObj rpcObj, AppInfo appInfo);

    RpcObj cal(RpcObj rpcObj, AppInfo appInfo);

    void intMethod(int aaa, AppInfo appInfo);

    Pong ping(AppInfo appInfo);
}
