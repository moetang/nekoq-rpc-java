package net.moetang.nekoq.rpc.core;

/**
 * Created by sunhao on 16-2-28.
 */
public class PacketConstants {
    // max length
    public static final int MAX_LENGTH = 64 * 1024;

    // header options:
    public static final int OPTION_LENGTH = 1 << 0;

    // packet type
    public static final int PACKET_RPC = 0x03;
    // packet sub type
    public static final int PACKET_RPC_REQ = 0x01;
    public static final int PACKET_RPC_RESP = 0x02;
}
