package net.moetang.nekoq.rpc.client;

import net.moetang.nekoq.rpc.core.RpcResp;

import java.util.concurrent.CountDownLatch;

/**
 * Created by sunhao on 16-3-1.
 */
class ClientReq {
    final CountDownLatch countDownLatch = new CountDownLatch(1);
    RpcResp resp;
}
