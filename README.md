# nekoq-rpc-java
nekoq-rpc-java server/client

## Usage Example:

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

1. interface without inheritance
2. method interface should be: 1 or 2 params with last param type of AppInfo
3. no or 1 return value
4. method name should not be override
5. bean must contain a default constructor & getters/setters for every field
