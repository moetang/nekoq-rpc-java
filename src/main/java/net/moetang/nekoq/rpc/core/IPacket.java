package net.moetang.nekoq.rpc.core;

import io.netty.buffer.ByteBuf;

/**
 * Created by sunhao on 16-2-28.
 */
public interface IPacket {
    int writeTo(ByteBuf byteBuf);
}
