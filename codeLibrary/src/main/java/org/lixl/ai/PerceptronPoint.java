package org.lixl.ai;

/**
 * Created by Administrator on 3/16/2018.
 * 分类用。包含两部分：x表示输入R维空间向量，y表示分类值，只有-1和+1两类。
 */
public class PerceptronPoint {
    //x表示 输入R维空间向量
    double[] x = new double[2];
    //y表示 分类值，只有-1和+1两类
    double y = 0;

    PerceptronPoint(double[] x, double y){
        this.x = x;
        this.y = y;
    }
    public PerceptronPoint(){

    }
}
