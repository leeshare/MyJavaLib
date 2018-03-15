package org.lixl.ai;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Administrator on 3/14/2018.
 * 感知机分类算法——对偶形式
 */
public class PerceptronClassifierDual {

    //分类器参数
    private double[] w;         //权值向量组
    private double b = 0;       //偏置
    private double eta = 1;     // 步长/学习率  η
    ArrayList<Point> instanceList;
    private double[] alpha;     // α 列表
    private double[][] gram;

    public PerceptronClassifierDual(ArrayList<Point> instanceList, double eta){
        this.instanceList = instanceList;
        w = new double[instanceList.get(0).x.length];
        this.eta = eta;
        alpha = new double[instanceList.size()];
    }
    public PerceptronClassifierDual(ArrayList<Point> instanceList){
        this.instanceList = instanceList;
        w = new double[instanceList.get(0).x.length];
        this.eta = 1;
        alpha = new double[instanceList.size()];
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

    private void CalcGramMatrix(){
        if(instanceList.size() <= 0){
            return;
        }
        //x 只能是两个元素，否则没法算了吧？
        int num = alpha.length;
        gram = new double[num][num];
        for(int i =0; i < num; i++){
            for(int j = 0; j < num; j++){
                gram[i][j] = instanceList.get(i).x[0] * instanceList.get(j).x[1] + instanceList.get(j).x[1] * instanceList.get(i).x[0];
            }
        }
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
        PerceptronClassifierDual classifier = new PerceptronClassifierDual(list);
        classifier.Classify();
    }

    public static void main(String[] args){
        instance1();
    }
}

