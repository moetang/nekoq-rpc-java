package net.moetang.nekoq.rpc.core;

import io.netty.buffer.ByteBuf;
import net.moetang.nekoq.rpc.util.Utils;

/**
 * Created by sunhao on 16-2-28.
 */
public class RpcResp implements IPacket {
    private int reqId;
    private int resultType;
    private byte[] result;

    public int getReqId() {
        return reqId;
    }

    public void setReqId(int reqId) {
        this.reqId = reqId;
    }

    public int getResultType() {
        return resultType;
    }

    public void setResultType(int resultType) {
        this.resultType = resultType;
    }

    public byte[] getResult() {
        return result;
    }

    public void setResult(byte[] result) {
        this.result = result;
    }

    public int writeTo(ByteBuf byteBuf) {
        int len = 12;
        if (result != null) {
            len += result.length;
        }
        Utils.writeIntBE(byteBuf, len);
        Utils.writeIntBE(byteBuf, this.reqId);
        Utils.writeIntBE(byteBuf, this.resultType);
        if (result != null) {
            Utils.writeIntBE(byteBuf, result.length);
            byteBuf.writeBytes(this.result);
        } else {
            Utils.writeIntBE(byteBuf, 0);
        }
        return -1;
    }
}
