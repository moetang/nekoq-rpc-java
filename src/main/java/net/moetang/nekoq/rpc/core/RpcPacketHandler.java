package net.moetang.nekoq.rpc.core;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.moetang.nekoq.rpc.RpcRuntimeException;

/**
 * Created by sunhao on 16-2-28.
 */
public class RpcPacketHandler extends ChannelInboundHandlerAdapter {
    private RpcHandler rpcHandler;

    public RpcPacketHandler(RpcHandler rpcHandler) {
        this.rpcHandler = rpcHandler;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof RpcReq) {
            RpcReq req = (RpcReq) msg;
            this.rpcHandler.handleRpcReq(ctx, req);
        } else if (msg instanceof RpcResp) {
            RpcResp resp = (RpcResp) msg;
            this.rpcHandler.handleRpcResp(ctx, resp);
        } else {
            throw new RpcRuntimeException("unknown msg type.");
        }
    }
}
