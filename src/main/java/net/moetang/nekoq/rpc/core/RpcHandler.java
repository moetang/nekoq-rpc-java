package net.moetang.nekoq.rpc.core;

import io.netty.channel.ChannelHandlerContext;

/**
 * Created by sunhao on 16-2-28.
 */
public interface RpcHandler {
    void handleRpcReq(ChannelHandlerContext ctx, RpcReq req) throws Exception;

    void handleRpcResp(ChannelHandlerContext ctx, RpcResp resp) throws Exception;
}
