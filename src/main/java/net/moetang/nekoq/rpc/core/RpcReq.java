package net.moetang.nekoq.rpc.core;

import io.netty.buffer.ByteBuf;
import net.moetang.nekoq.rpc.RpcRuntimeException;
import net.moetang.nekoq.rpc.util.Utils;

/**
 * Created by sunhao on 16-2-28.
 */
public class RpcReq implements IPacket {
    private static final byte[] EMPTY_DATA = new byte[0];

    private byte[] traceId;
    private byte[] rpcId;
    private int reqId;
    private String serviceName;
    private byte[] paramData;

    public byte[] getTraceId() {
        return traceId;
    }

    public void setTraceId(byte[] traceId) {
        this.traceId = traceId;
    }

    public byte[] getRpcId() {
        return rpcId;
    }

    public void setRpcId(byte[] rpcId) {
        this.rpcId = rpcId;
    }

    public int getReqId() {
        return reqId;
    }

    public void setReqId(int reqId) {
        this.reqId = reqId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public byte[] getParamData() {
        return paramData;
    }

    public void setParamData(byte[] paramData) {
        this.paramData = paramData;
    }

    public int writeTo(ByteBuf byteBuf) {
        int startWriteIdx = byteBuf.writerIndex();
        Utils.writeIntBE(byteBuf, 0);
        if (this.traceId != null && this.traceId.length == 64) {
            byteBuf.writeBytes(this.traceId);
        } else {
            throw new RpcRuntimeException("traceId is not 64bytes");
        }
        if (this.rpcId != null && this.rpcId.length == 32) {
            byteBuf.writeBytes(this.rpcId);
        } else {
            throw new RpcRuntimeException("rpcId is not 32 bytes");
        }
        Utils.writeIntBE(byteBuf, this.reqId);
        Utils.writeBytes16BE(byteBuf, this.serviceName.getBytes());
        if (this.paramData == null) {
            this.paramData = EMPTY_DATA;
        }
        Utils.writeBytes32BE(byteBuf, this.paramData);
        int endWriteIdx = byteBuf.writerIndex();
        Utils.setIntBE(byteBuf, startWriteIdx, endWriteIdx - startWriteIdx - 4);
        return -1;
    }
}
