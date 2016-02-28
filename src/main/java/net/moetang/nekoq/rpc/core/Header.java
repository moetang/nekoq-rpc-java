package net.moetang.nekoq.rpc.core;

import io.netty.buffer.ByteBuf;

/**
 * Created by sunhao on 16-2-28.
 */
public class Header {

    private int version;
    private int packetType;
    private int subPacketType;
    private int option;
    private int rest;

    private int bodyLength;

    private ByteBuf bodyData;

    public static Header of(ByteBuf byteBuf) {
        Header header = new Header();
        byte b = byteBuf.readByte();
        header.version = b & 0xff;
        header.packetType = b & 0x3f;
        b = byteBuf.readByte();
        header.subPacketType = b & 0xff;
        b = byteBuf.readByte();
        header.option = b & 0xff;
        b = byteBuf.readByte();
        header.rest = b & 0xff;
        return header;
    }

    public static void writeTo(ByteBuf byteBuf, IPacket packet, int type, int subType, int option) {
        byteBuf.writeByte(PacketConstants.VERSION_1 | type);
        byteBuf.writeByte(subType);
        byteBuf.writeByte(option);
        byteBuf.writeByte(0);
        packet.writeTo(byteBuf);
    }

    int getBodyLength() {
        return bodyLength;
    }

    void setBodyLength(int bodyLength) {
        this.bodyLength = bodyLength;
    }

    ByteBuf getBodyData() {
        return bodyData;
    }

    void setBodyData(ByteBuf bodyData) {
        this.bodyData = bodyData;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getPacketType() {
        return packetType;
    }

    public void setPacketType(int packetType) {
        this.packetType = packetType;
    }

    public int getSubPacketType() {
        return subPacketType;
    }

    public void setSubPacketType(int subPacketType) {
        this.subPacketType = subPacketType;
    }

    public int getOption() {
        return option;
    }

    public void setOption(int option) {
        this.option = option;
    }

    public int getRest() {
        return rest;
    }

    public void setRest(int rest) {
        this.rest = rest;
    }

    public boolean hasBody() {
        return (this.option & PacketConstants.OPTION_LENGTH) != 0;
    }
}
