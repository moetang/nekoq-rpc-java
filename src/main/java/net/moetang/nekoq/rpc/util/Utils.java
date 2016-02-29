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
        int len = buf.readInt();
        if (len > maxLength) {
            throw new IndexOutOfBoundsException("length field is greater than maxLength");
        }
        byte[] tmp = new byte[len];
        buf.readBytes(tmp);
        return tmp;
    }

    public static void writeIntBE(ByteBuf buf, int i) {
        byte b1 = (byte) (i >> 24);
        byte b2 = (byte) (i >> 16);
        byte b3 = (byte) (i >> 8);
        byte b4 = (byte) i;
        buf.writeByte(b1).writeByte(b2).writeByte(b3).writeByte(b4);
    }

    public static void setIntBE(ByteBuf buf, int idx, int i) {
        byte b1 = (byte) (i >> 24);
        byte b2 = (byte) (i >> 16);
        byte b3 = (byte) (i >> 8);
        byte b4 = (byte) i;
        buf.setByte(idx++, b1).setByte(idx++, b2).setByte(idx++, b3).setByte(idx, b4);
    }

    public static void writeInt16BE(ByteBuf buf, int i) {
        byte b1 = (byte) (i >> 8);
        byte b2 = (byte) i;
        buf.writeByte(b1).writeByte(b2);
    }

    public static void writeBytes16BE(ByteBuf buf, byte[] bytes) {
        int len = bytes.length;
        writeInt16BE(buf, len);
        buf.writeBytes(bytes);
    }

    public static void writeBytes32BE(ByteBuf buf, byte[] bytes) {
        int len = bytes.length;
        writeIntBE(buf, len);
        buf.writeBytes(bytes);
    }
}
