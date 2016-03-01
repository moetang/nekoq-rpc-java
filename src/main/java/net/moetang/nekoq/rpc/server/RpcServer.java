package net.moetang.nekoq.rpc.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import net.moetang.nekoq.rpc.RpcRuntimeException;
import net.moetang.nekoq.rpc.core.*;
import org.msgpack.jackson.dataformat.MessagePackFactory;

import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sunhao on 16-3-1.
 */
public class RpcServer {
    private static final ObjectMapper mapper = new ObjectMapper(new MessagePackFactory());

    private ServerBootstrap bootstrap;
    private Channel channel;
    private Object serviceImpl;
    private Class<?> interfaceClass;
    private Map<String, Method> methodMap = new HashMap<String, Method>();

    private void init() {
        Method[] methods = interfaceClass.getDeclaredMethods();
        if (methods == null || methods.length == 0) {
            throw new RpcRuntimeException("no method given.");
        }

        for (Method method : methods) {
            checkMethod(method);
            methodMap.put(method.getName(), method);
        }
    }

    private void checkMethod(Method method) {
        Class<?>[] types = method.getParameterTypes();
        if (types == null || types.length > 2 || types.length < 1) {
            throw new RpcRuntimeException("method param count is invalid.");
        }

        if (!AppInfo.class.isAssignableFrom(types[types.length - 1])) {
            throw new RpcRuntimeException("last param is not AppInfo");
        }
    }

    public static <T, R extends T> RpcServer create(InetSocketAddress bindAddr, R serviceImpl, Class<T> interfaceClass) {
        final RpcServer server = new RpcServer();

        server.serviceImpl = serviceImpl;
        server.interfaceClass = interfaceClass;
        server.init();

        server.bootstrap = new ServerBootstrap();
        try {
            server.channel = server.bootstrap.group(new NioEventLoopGroup(), new NioEventLoopGroup())
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new HeaderPacketDecoder(PacketConstants.MAX_LENGTH));
                            ch.pipeline().addLast(new RpcPacketDecoder());
                            ch.pipeline().addLast(new RpcPacketEncoder());
                            ch.pipeline().addLast(new RpcPacketHandler(new RpcHandler() {
                                private Map<String, Method> methodMap = server.methodMap;
                                private Object serviceImpl = server.serviceImpl;

                                public void handleRpcReq(ChannelHandlerContext ctx, RpcReq req) throws Exception {
                                    String errorMessage = null;
                                    Object result = null;

                                    Method method = methodMap.get(req.getServiceName());
                                    try {
                                        do {
                                            if (method == null) {
                                                errorMessage = "no method found: " + req.getServiceName();
                                                break;
                                            }
                                            Class<?>[] types = method.getParameterTypes();
                                            if (types.length == 1) {
                                                result = method.invoke(serviceImpl, new AppInfo());
                                            } else if (types.length == 2) {
                                                result = method.invoke(serviceImpl, mapper.readValue(req.getParamData(), types[0]), new AppInfo());
                                            }
                                        } while (false);
                                    } catch (Exception e) {
                                        errorMessage = e.getClass() + ": " + e.getMessage();
                                    }

                                    RpcResp resp = new RpcResp();
                                    resp.setReqId(req.getReqId());
                                    if (errorMessage != null) {
                                        resp.setResultType(1);
                                        resp.setResult(mapper.writeValueAsBytes(errorMessage));
                                    } else {
                                        resp.setResultType(0);
                                        if (method.getReturnType() == void.class || method.getReturnType() == Void.class) {
                                            resp.setResult(null);
                                        } else if (result == null) {
                                            resp.setResult(null);
                                        } else {
                                            resp.setResult(mapper.writeValueAsBytes(result));
                                        }
                                    }
                                    ctx.writeAndFlush(resp, ctx.voidPromise());
                                }

                                public void handleRpcResp(ChannelHandlerContext ctx, RpcResp resp) throws Exception {
                                    throw new RpcRuntimeException("unsupported.");
                                }
                            }));
                        }
                    })
                    .bind(bindAddr).sync().channel();
        } catch (Exception e) {
            throw new RpcRuntimeException("start rpc server error.", e);
        }

        return server;
    }
}
