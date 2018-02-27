package org.lixl.acm;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2/27/2018.
 * 根据输入的两行数字，比较之，求排名没有变化的人的最大数量
 * 比如  2  3    和  3  2  最大没有变化的是 1 ，而不是 0 ，
 *      因为 可能只有 3 的积分增加了，而2没变，导致3拍到了第一位
 *      也可能只有 2 的积分减少了，而3没变，导致3拍到了第一位。
 */
public class AcmRanking {
    static int inputNum = 4;
    static int[] inputOld = {2,3,1,4,5};
    static int[] inputNew = {3,2,1,4,5};
    //由于输入4，那么就不考虑第5个元素。那么最少就只有一个元素的值改了，那么 输出结果应该是 3

    static class RankingElement{
        //元素的前一个名称
        int last;
        //元素名称
        int name;
        //元素的后一个名称
        int next;

        public int getName(){return name;}
        public int getLast(){return last;}
        public int getNext(){return next;}
        public void setName(int name){this.name = name;}
        public void setLast(int last){this.last = last;}
        public void setNext(int next){this.next = last;}
    }

    private static List createList(int[] input){
        if(inputNum > input.length)
            return null;
        List list = new ArrayList<RankingElement>();
        for(int i = 0; i < inputNum; i++){
            RankingElement element = new RankingElement();
            if(i == 0){
                element.last = 0;
            }else{
                if(i == input.length - 1) {
                    element.next = 0;
                }
                RankingElement lastElement = (RankingElement) list.get(i - 1);
                lastElement.next = input[i];
                element.last = lastElement.getName();
            }
            element.name = input[i];
            list.add(element);
        }
        return list;
    }

    public static void main(String[] args){
        List listOld = new ArrayList<RankingElement>();
        List listNew = new ArrayList<RankingElement>();
        listOld = createList(inputOld);
        listNew = createList(inputNew);

    }
}
