package net.moetang.nekoq.rpc.core;

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
}
