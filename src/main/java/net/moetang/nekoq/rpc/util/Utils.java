package net.moetang.nekoq.rpc.util;

import io.netty.buffer.ByteBuf;

/**
 * Created by sunhao on 16-2-28.
 */
public class Utils {
    public static String readString16BE(ByteBuf buf, int maxLength) {
        int len = buf.readUnsignedShort();
        if (len > maxLength) {
            throw new IndexOutOfBoundsException("length field is greater than maxLength");
        }
        byte[] tmp = new byte[len];
        buf.readBytes(tmp);
        return new String(tmp);
    }

    public static byte[] readBytes32BE(ByteBuf buf, int maxLength) {
        int len = buf.readUnsignedShort();
        if (len > maxLength) {
            throw new IndexOutOfBoundsException("length field is greater than maxLength");
        }
        byte[] tmp = new byte[len];
        buf.readBytes(tmp);
        return tmp;
    }
}
