package org.lixl.ai;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Administrator on 3/9/2018.
 *
 * 感知机算法——原始形式
 */
public class PerceptronClassifier {
    //分类器参数
    private double[] w;         //权值向量组
    private double b = 0;       //偏置
    private double eta = 1;     // 步长/学习率  η
    ArrayList<PerceptronPoint> instanceList;

    public PerceptronClassifier(ArrayList<PerceptronPoint> instanceList, double eta){
        this.instanceList = instanceList;
        w = new double[instanceList.get(0).x.length];
        this.eta = eta;
    }
    public PerceptronClassifier(ArrayList<PerceptronPoint> arrayList){
        this.instanceList = arrayList;
        w = new double[arrayList.get(0).x.length];
        this.eta = 1;
    }

    /**
     * 进行分类计算
     * @return 是否分类成功
     */
    public boolean Classify(){
        boolean flag = false;
        while(!flag){
            for(int i = 0; i < instanceList.size(); i++){  //所有训练集
                if(LearnAnswer(instanceList.get(i)) <= 0){
                    UpdateWAndB(instanceList.get(i));
                    break;
                }
                if(i == (instanceList.size() - 1)){
                    flag = true;
                }
            }
        }
        System.out.println(Arrays.toString(w));
        System.out.println(b);

        return true;
    }

    /**
     * 进行学习得到的结果
     * @param point 一个训练样本
     * @return
     */
    private double LearnAnswer(PerceptronPoint point){
        System.out.println(Arrays.toString(w));
        System.out.println(b);
        //y * (w1 * x1 + w2 * x2 + b)
        return point.y * (DotProduct(w, point.x) + b);
    }

    /**
     * 进行点乘
     * @param x1
     * @param x2
     * @return
     */
    private double DotProduct(double[] x1, double[] x2){
        int len = x1.length;
        double sum = 0;
        for(int i = 0; i < len; i++){
            sum += x1[i] * x2[i];
        }
        return sum;
    }

    /**
     * 进行w和b的更新
     * @param point 需要根据样本来随机梯度下降 来进行w和b更新
     */
    private void UpdateWAndB(PerceptronPoint point){
        for(int i = 0; i < w.length; i++){
            w[i] += eta * point.y * point.x[i];
        }
        b += eta * point.y;
    }

    private static void instance1(){
        //最终结果是  y = sign(1* x1 + 1* x2 - 3)
        PerceptronPoint p1 = new PerceptronPoint(new double[]{3, 3}, 1);
        PerceptronPoint p2 = new PerceptronPoint(new double[]{4, 3}, 1);
        PerceptronPoint p3 = new PerceptronPoint(new double[]{1, 1}, -1);
        ArrayList<PerceptronPoint> list = new ArrayList<PerceptronPoint>();
        list.add(p1);
        list.add(p2);
        list.add(p3);
        PerceptronClassifier classifier = new PerceptronClassifier(list);
        classifier.Classify();
    }
    private static void instance2(){
        //最终结果是  y = sign(1* x1 + 1* x2 - 3)
        PerceptronPoint p1 = new PerceptronPoint(new double[]{0, 0, 0, 1}, -1);
        PerceptronPoint p2 = new PerceptronPoint(new double[]{1, 0, 0, 0}, 1);
        PerceptronPoint p3 = new PerceptronPoint(new double[]{2, 1, 0, 0}, 1);
        PerceptronPoint p4 = new PerceptronPoint(new double[]{2, 1, 0, 1}, -1);

        ArrayList<PerceptronPoint> list = new ArrayList<PerceptronPoint>();
        list.add(p1);
        list.add(p2);
        list.add(p3);
        list.add(p4);
        PerceptronClassifier classifier = new PerceptronClassifier(list);
        classifier.Classify();
    }

    public static void main(String[] args){
        instance1();
        //instance2();
    }

}


