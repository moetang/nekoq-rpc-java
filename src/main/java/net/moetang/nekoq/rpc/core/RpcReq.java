package net.moetang.nekoq.rpc.core;

/**
 * Created by sunhao on 16-2-28.
 */
public class RpcReq implements IPacket {
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
}
