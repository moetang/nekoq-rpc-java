package net.moetang.nekoq.rpc.test;

/**
 * Created by sunhao on 16-3-1.
 */
public class RpcObj {
    private String hahaha;
    private int age;

    public RpcObj() {
    }

    public RpcObj(String hahaha, int age) {
        this.hahaha = hahaha;
        this.age = age;
    }

    public String getHahaha() {
        return hahaha;
    }

    public void setHahaha(String hahaha) {
        this.hahaha = hahaha;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "RpcObj{" +
                "hahaha='" + hahaha + '\'' +
                ", age=" + age +
                '}';
    }
}
