package org.lixl.ai;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Administrator on 3/9/2018.
 *
 * 感知机算法
 */
public class PerceptronClassifier {
    //分类器参数
    private double[] w;         //权值向量组
    private double b = 0;       //偏置
    private double eta = 1;     // 步长/学习率  η
    ArrayList<Point> arrayList;

    public PerceptronClassifier(ArrayList<Point> arrayList, double eta){
        this.arrayList = arrayList;
        w = new double[arrayList.get(0).x.length];
        this.eta = eta;
    }
    public PerceptronClassifier(ArrayList<Point> arrayList){
        this.arrayList = arrayList;
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
            for(int i = 0; i < arrayList.size(); i++){  //所有训练集
                if(LearnAnswer(arrayList.get(i)) <= 0){
                    UpdateWAndB(arrayList.get(i));
                    break;
                }
                if(i == (arrayList.size() - 1)){
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
    private double LearnAnswer(Point point){
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
    private void UpdateWAndB(Point point){
        for(int i = 0; i < w.length; i++){
            w[i] += eta * point.y * point.x[i];
        }
        b += eta * point.y;
    }

    private static void instance1(){
        //最终结果是  y = sign(1* x1 + 1* x2 - 3)
        Point p1 = new Point(new double[]{3, 3}, 1);
        Point p2 = new Point(new double[]{4, 3}, 1);
        Point p3 = new Point(new double[]{1, 1}, -1);
        ArrayList<Point> list = new ArrayList<Point>();
        list.add(p1);
        list.add(p2);
        list.add(p3);
        PerceptronClassifier classifier = new PerceptronClassifier(list);
        classifier.Classify();
    }
    private static void instance2(){
        //最终结果是  y = sign(1* x1 + 1* x2 - 3)
        Point p1 = new Point(new double[]{0, 0, 0, 1}, -1);
        Point p2 = new Point(new double[]{1, 0, 0, 0}, 1);
        Point p3 = new Point(new double[]{2, 1, 0, 0}, 1);
        Point p4 = new Point(new double[]{2, 1, 0, 1}, -1);

        ArrayList<Point> list = new ArrayList<Point>();
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

/**
 * 分类用。包含两部分：x表示输入R维空间向量，y表示分类值，只有-1和+1两类。
 */
class Point {
    //x表示 输入R维空间向量
    double[] x = new double[2];
    //y表示 分类值，只有-1和+1两类
    double y = 0;

    Point(double[] x, double y){
        this.x = x;
        this.y = y;
    }
    public Point(){

    }
}

