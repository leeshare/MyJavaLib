package org.lixl.ai.ga.simple;

public class AllOnesGA {

    public static void main(String[] args) {
        //创建GA对象
        GeneticAlgorithm ga = new GeneticAlgorithm(100, 0.001, 0.95, 2);
        //初始化种群
        Population population = ga.initPopulation(50);
        //进化种群
        ga.evalPopulation(population);
        //设置当前代的追踪
        int generation = 1;

        //开始进化循环
        while(ga.isTerminationConditionMet(population) == false) {
            //打印种群中的最适合个体
            System.out.println("第" + generation + "代最佳解决方案：" + population.getFittest(0).toString());
            //应用交叉
            population = ga.crossoverPopulation(population);
            //应用变异
            population = ga.mutatePopulation(population);
            //进化种群
            ga.evalPopulation(population);
            //增加当前代
            generation++;
        }

        System.out.println("找到解决方案在第 " + generation + " 代");
        System.out.println("最佳解决方案：" + population.getFittest(0).toString());

    }
}
