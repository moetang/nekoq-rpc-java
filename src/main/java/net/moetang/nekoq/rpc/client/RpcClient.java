package net.moetang.nekoq.rpc.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import net.moetang.nekoq.rpc.RpcExecutionException;
import net.moetang.nekoq.rpc.RpcRuntimeException;
import net.moetang.nekoq.rpc.core.*;
import org.msgpack.jackson.dataformat.MessagePackFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by sunhao on 16-3-1.
 */
public class RpcClient<T> {
    private static final ObjectMapper mapper = new ObjectMapper(new MessagePackFactory());

    private InetSocketAddress address;
    private Bootstrap bootstrap;
    private Channel channel;
    private Class<T> serviceInterfaceClass;
    private T instance;

    private AtomicInteger integer = new AtomicInteger(0);
    private Map<Integer, ClientReq> requests = new ConcurrentHashMap<Integer, ClientReq>();

    private RpcClient() {
    }

    private byte[] getTraceId(AppInfo appInfo) {
        //TODO
        return new byte[64];
    }

    private byte[] getRpcId(AppInfo appInfo) {
        //TODO
        return new byte[32];
    }

    private void init() {
        Method[] methods = serviceInterfaceClass.getDeclaredMethods();
        if (methods == null || methods.length == 0) {
            throw new RpcRuntimeException("no method given.");
        }

        for (Method method : methods) {
            checkMethod(method);
        }

        Object object = Proxy.newProxyInstance(serviceInterfaceClass.getClassLoader(), new Class[]{serviceInterfaceClass}, new InvocationHandler() {
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if (args.length == 1) {
                    // no param
                    AppInfo appInfo = (AppInfo) args[0];
                    ClientReq req = new ClientReq();
                    int reqId = integer.incrementAndGet();
                    RpcReq rpcReq = new RpcReq();
                    rpcReq.setTraceId(getTraceId(appInfo));
                    rpcReq.setRpcId(getRpcId(appInfo));
                    rpcReq.setReqId(reqId);
                    rpcReq.setServiceName(method.getName());
                    requests.put(reqId, req);
                    channel.writeAndFlush(rpcReq);
                    req.countDownLatch.await();
                    RpcResp resp = req.resp;
                    if (resp.getResultType() == 1) {
                        if (resp.getResult() == null) {
                            throw new RpcExecutionException("no error message return.");
                        } else {
                            throw new RpcExecutionException(new String(resp.getResult()));
                        }
                    } else if (resp.getResultType() == 0) {
                        if (method.getReturnType() == void.class || method.getReturnType() == Void.class) {
                            return null;
                        } else if (resp.getResult() == null) {
                            return null;
                        } else {
                            return mapper.readValue(resp.getResult(), method.getReturnType());
                        }
                    } else {
                        throw new RpcRuntimeException("result type unknown: " + resp.getResultType());
                    }
                } else if (args.length == 2) {
                    // one param
                    AppInfo appInfo = (AppInfo) args[1];
                    ClientReq req = new ClientReq();
                    int reqId = integer.incrementAndGet();
                    RpcReq rpcReq = new RpcReq();
                    rpcReq.setTraceId(getTraceId(appInfo));
                    rpcReq.setRpcId(getRpcId(appInfo));
                    rpcReq.setReqId(reqId);
                    rpcReq.setServiceName(method.getName());
                    rpcReq.setParamData(mapper.writeValueAsBytes(args[0]));
                    requests.put(reqId, req);
                    channel.writeAndFlush(rpcReq);
                    req.countDownLatch.await();
                    RpcResp resp = req.resp;
                    if (resp.getResultType() == 1) {
                        if (resp.getResult() == null) {
                            throw new RpcExecutionException("no error message return.");
                        } else {
                            throw new RpcExecutionException(new String(resp.getResult()));
                        }
                    } else if (resp.getResultType() == 0) {
                        if (method.getReturnType() == void.class || method.getReturnType() == Void.class) {
                            return null;
                        } else if (resp.getResult() == null) {
                            return null;
                        } else {
                            return mapper.readValue(resp.getResult(), method.getReturnType());
                        }
                    } else {
                        throw new RpcRuntimeException("result type unknown: " + resp.getResultType());
                    }
                }
                throw new RpcRuntimeException("param is not matched.");
            }
        });

        this.instance = (T) object;
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

    private void start() throws InterruptedException {
        this.bootstrap = new Bootstrap();
        Channel channel = bootstrap.channel(NioSocketChannel.class)
                .group(new NioEventLoopGroup())
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new HeaderPacketDecoder(PacketConstants.MAX_LENGTH));
                        ch.pipeline().addLast(new RpcPacketDecoder());
                        ch.pipeline().addLast(new RpcPacketEncoder());
                        ch.pipeline().addLast(new RpcPacketHandler(new RpcHandler() {
                            public void handleRpcReq(ChannelHandlerContext ctx, RpcReq req) throws Exception {
                                throw new RpcRuntimeException("unsupported.");
                            }

                            public void handleRpcResp(ChannelHandlerContext ctx, RpcResp resp) throws Exception {
                                ClientReq req = requests.remove(resp.getReqId());
                                if (req != null) {
                                    req.resp = resp;
                                    req.countDownLatch.countDown();
                                }
                            }
                        }));
                    }
                })
                .connect(this.address).sync().channel();
        this.channel = channel;
    }

    public static <T> RpcClient<T> create(InetSocketAddress address, Class<T> serviceInterfaceClass) {
        RpcClient<T> rpcClient = new RpcClient<T>();
        rpcClient.address = address;
        rpcClient.serviceInterfaceClass = serviceInterfaceClass;

        rpcClient.init();

        try {
            rpcClient.start();
        } catch (Exception e) {
            throw new RpcRuntimeException("start rpc client error.", e);
        }

        return rpcClient;
    }

    public T getInstance() {
        return this.instance;
    }
}
