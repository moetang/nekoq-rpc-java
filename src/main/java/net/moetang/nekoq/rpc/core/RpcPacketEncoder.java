package net.moetang.nekoq.rpc.core;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import net.moetang.nekoq.rpc.RpcRuntimeException;

/**
 * Created by sunhao on 16-2-28.
 */
public class RpcPacketEncoder extends MessageToByteEncoder<IPacket> {
    @Override
    protected void encode(ChannelHandlerContext ctx, IPacket msg, ByteBuf out) throws Exception {
        if (msg instanceof RpcReq) {
            RpcReq req = (RpcReq) msg;
            encodeRpcReq(req, out);
        } else if (msg instanceof RpcResp) {
            RpcResp resp = (RpcResp) msg;
            encodeRpcResp(resp, out);
        } else {
            throw new RpcRuntimeException("unknown packet type.");
        }
    }

    private void encodeRpcResp(RpcResp resp, ByteBuf out) {
        Header.writeTo(out, resp, PacketConstants.PACKET_RPC, PacketConstants.PACKET_RPC_RESP, PacketConstants.OPTION_LENGTH);
    }

    private void encodeRpcReq(RpcReq req, ByteBuf out) {
        Header.writeTo(out, req, PacketConstants.PACKET_RPC, PacketConstants.PACKET_RPC_REQ, PacketConstants.OPTION_LENGTH);
    }
}
