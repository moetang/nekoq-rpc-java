package net.moetang.nekoq.rpc.core;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import net.moetang.nekoq.rpc.RpcRuntimeException;

import java.nio.ByteOrder;
import java.util.List;

/**
 * Created by sunhao on 16-2-28.
 */
public class HeaderPacketDecoder extends ByteToMessageDecoder {
    private Header h;
    private int lastReadLength = 0;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        Object decoded = decode(ctx, in);
        if (decoded != null) {
            out.add(decoded);
        }
    }

    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        if (this.lastReadLength > 0) {
            in.skipBytes(this.lastReadLength);
            this.lastReadLength = 0;
        }

        if (in.readableBytes() < 4) {
            return null;
        }

        // read head
        if (this.h == null) {
            Header header = Header.of(in);

            if (!header.hasBody()) {
                return header;
            }

            this.h = header;
        }
        in = in.order(ByteOrder.BIG_ENDIAN);
        Header header = this.h;
        int bodyLength = header.getBodyLength();

        // read body length
        if (bodyLength <= 0) {
            if (in.readableBytes() < 4) {
                return null;
            }
            int len = in.readInt();
            if (len <= 0) {
                throw new RpcRuntimeException("body length should > 0, but got " + len);
            }
            header.setBodyLength(len);
        }

        if (bodyLength > in.readableBytes()) {
            return null;
        }

        this.lastReadLength = bodyLength;
        header.setBodyData(in.slice(in.readerIndex(), bodyLength).retain());
        this.h = null;
        return header;
    }
}