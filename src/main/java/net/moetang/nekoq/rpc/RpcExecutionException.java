package net.moetang.nekoq.rpc;

/**
 * Created by sunhao on 16-3-1.
 */
public class RpcExecutionException extends RpcRuntimeException {
    public RpcExecutionException() {
        super();
    }

    public RpcExecutionException(String message) {
        super(message);
    }

    public RpcExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

    public RpcExecutionException(Throwable cause) {
        super(cause);
    }

    public RpcExecutionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
