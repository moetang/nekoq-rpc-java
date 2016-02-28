package net.moetang.nekoq.rpc.core;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import net.moetang.nekoq.rpc.RpcRuntimeException;
import net.moetang.nekoq.rpc.util.Utils;

import java.nio.ByteOrder;
import java.util.List;

/**
 * Created by sunhao on 16-2-28.
 */
public class RpcPacketDecoder extends MessageToMessageDecoder<Header> {
    @Override
    protected void decode(ChannelHandlerContext ctx, Header msg, List<Object> out) throws Exception {
        try {
            Object decoded = decode(ctx, msg);
            if (decoded != null) {
                out.add(decoded);
            }
        } finally {
            if (msg != null && msg.getBodyData() != null) {
                msg.getBodyData().release();
                msg.setBodyData(null);
            }
        }
    }

    protected Object decode(ChannelHandlerContext ctx, Header msg) throws Exception {
        if (msg.getPacketType() != PacketConstants.PACKET_RPC) {
            throw new RpcRuntimeException("packet type is not rpc: " + msg.getPacketType());
        }
        if (msg.getBodyData() == null) {
            throw new RpcRuntimeException("rpc packet without body.");
        }
        switch (msg.getSubPacketType()) {
            case PacketConstants.PACKET_RPC_REQ:
                return decodeRpcReq(msg);
            case PacketConstants.PACKET_RPC_RESP:
                return decodeRpcResp(msg);
            default:
                throw new RpcRuntimeException("sub packet type unknown: " + msg.getSubPacketType());
        }
    }

    private Object decodeRpcResp(Header msg) {
        RpcResp resp = new RpcResp();
        ByteBuf data = msg.getBodyData();
        data = data.order(ByteOrder.BIG_ENDIAN);

        int reqId = data.readInt();
        resp.setReqId(reqId);
        int resultType = data.readInt();
        resp.setResultType(resultType);
        resp.setResult(Utils.readBytes32BE(data, PacketConstants.MAX_LENGTH));
        return resp;
    }

    private Object decodeRpcReq(Header msg) {
        RpcReq req = new RpcReq();
        ByteBuf data = msg.getBodyData();
        data = data.order(ByteOrder.BIG_ENDIAN);

        byte[] traceId = new byte[64];
        data.readBytes(traceId);
        req.setTraceId(traceId);

        byte[] rpcId = new byte[32];
        data.readBytes(rpcId);
        req.setRpcId(rpcId);

        int reqId = data.readInt();
        req.setReqId(reqId);

        req.setServiceName(Utils.readString16BE(data, PacketConstants.MAX_LENGTH));
        req.setParamData(Utils.readBytes32BE(data, PacketConstants.MAX_LENGTH));
        return req;
    }
}
