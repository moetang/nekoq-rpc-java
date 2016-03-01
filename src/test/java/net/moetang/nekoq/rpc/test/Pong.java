package net.moetang.nekoq.rpc.test;

/**
 * Created by sunhao on 16-3-1.
 */
public class Pong {
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "Pong{" +
                "msg='" + msg + '\'' +
                '}';
    }
}
