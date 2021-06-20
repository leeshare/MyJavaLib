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
    ArrayList<PerceptronPoint> instanceList;
    private double[] alpha;     // α 列表
    private double[][] gram;

    public PerceptronClassifierDual(ArrayList<PerceptronPoint> instanceList, double eta){
        this.instanceList = instanceList;
        w = new double[instanceList.get(0).x.length];
        this.eta = eta;
        alpha = new double[instanceList.size()];
    }
    public PerceptronClassifierDual(ArrayList<PerceptronPoint> instanceList){
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
        CalcGramMatrix();       //计算Gram矩阵
        boolean flag = false;
        while(!flag){
            for(int i = 0; i < instanceList.size(); i++){  //所有训练集
                if(LearnAnswer(instanceList.get(i), i) <= 0){
                    UpdateAlphaAndB(instanceList.get(i), i);
                    break;
                }
                if(i == (instanceList.size() - 1)){
                    flag = true;
                }
            }
        }
        //此处 每次遍历一遍 都可计算一次 w 和 b
        for(int k = 0; k < instanceList.size(); k++) {
            PerceptronPoint temp = instanceList.get(k);
            for (int l = 0; l < w.length; l++) {
                w[l] += alpha[k] * temp.y * temp.x[l];
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
                gram[i][j] = instanceList.get(i).x[0] * instanceList.get(j).x[0] + instanceList.get(i).x[1] * instanceList.get(j).x[1];
            }
        }
    }

    /**
     * 进行学习得到的结果
     * @param point 一个训练样本
     * @param index 当前传入point在instanceList中的索引
     * @return
     */
    private double LearnAnswer(PerceptronPoint point, int index){
        double result = 0;
        for(int i = 0; i < alpha.length; i++){
            PerceptronPoint p = instanceList.get(i);
            result += alpha[i] * p.y * gram[index][i];
        }
        return point.y * (result + b);
    }

    /**
     * 进行w和b的更新
     * @param point 需要根据样本来随机梯度下降 来进行b更新
     * @param index 需要修改 alhpa的索引
     */
    private void UpdateAlphaAndB(PerceptronPoint point, int index){
        alpha[index] += 1 * eta;
        b += point.y * eta;
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
        PerceptronClassifierDual classifier = new PerceptronClassifierDual(list);
        classifier.Classify();
    }

    public static void main(String[] args){
        instance1();
    }
}

