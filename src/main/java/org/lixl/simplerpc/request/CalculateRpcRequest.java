package org.lixl.simplerpc.request;

import java.io.Serializable;

/**
 * Created by Administrator on 11/7/2019.
 */
public class CalculateRpcRequest implements Serializable {

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

    private String method;
    private int a;
    private int b;

    @Override
    public String toString() {
        return "CalculateRpcRequest{" +
                "method='" + method + "\'" +
                ", a='" + a + "\'" +
                ", b='" + b + "\'" +
                "}";
    }
}
