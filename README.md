# nekoq-rpc-java
nekoq-rpc-java server/client

## Usage Example:

#####Service Interface

```
public interface IServiceDemo {
    Pong ping(AppInfo appInfo);
}
```

#####Service Impl

```
public class ServiceDemoImpl implements IServiceDemo {
    public Pong ping(AppInfo appInfo) {
        Pong pong = new Pong();
        pong.setMsg("pong");
        return pong;
    }
}
```

#####RpcServer

```
public class RpcServerTest {
    @Test
    public void testServerUsage() throws InterruptedException {
        IServiceDemo serviceDemo = new ServiceDemoImpl();
        RpcServer.create(new InetSocketAddress("127.0.0.1", 14357), serviceDemo, IServiceDemo.class);
        TimeUnit.SECONDS.sleep(10000000);
    }
}
```

#####RpcClient

```
public class RpcClientTest {
    @Test
    public void testRpcClientUsage() {
        RpcClient<IServiceDemo> rpcClient = RpcClient.create(new InetSocketAddress("127.0.0.1", 14357), IServiceDemo.class);
        IServiceDemo serviceDemo = rpcClient.getInstance();
        System.out.println(serviceDemo.ping(new AppInfo()));
    }
}
```

## Benchmark:

env: Core i5-2520M / 16G DDR3 / Ubuntu 15.10 with GNU/Linux 4.2.0-30-generic

TestCase: PingPong

1. server class: RpcServerTest - cold start
2. client class: RpcClientBench - cold start

Result:

```
time: 13816 , qps: 14475.969889982629
time: 10344 , qps: 19334.880123743234
time: 10275 , qps: 19464.7201946472
```

## Attention:

####rules of using rpc:

1. interface without inheritance
2. method interface should be: 1 or 2 params with last param type of AppInfo
3. no or 1 return value
4. method name should not be override
5. bean must contain a default constructor & getters/setters for every field
6. should not use generics in params or result
7. should not use recursive references in params/result
