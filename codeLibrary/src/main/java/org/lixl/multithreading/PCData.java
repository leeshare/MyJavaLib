package org.lixl.multithreading;

/**
 * Created by lxl on 18/6/20.
 */
public class PCData {
    private final int data;

    public PCData(int data){
        this.data = data;
    }
    public PCData(String d){
        data = Integer.valueOf(d);
    }

    @Override
    public String toString(){
        return "data:" + data;
    }

    /*public void setData(int data){
        this.data = data;
    }*/
    public int getData(){
        return data;
    }

}
