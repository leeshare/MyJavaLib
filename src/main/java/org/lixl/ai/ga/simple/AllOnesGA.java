package org.lixl.ai.ga.simple;

public class AllOnesGA {

    public static void main(String[] args) {
        //创建GA对象
        //GeneticAlgorithm ga = new GeneticAlgorithm(100, 0.001, 0.95, 2);
        GeneticAlgorithm ga = new GeneticAlgorithm(100, 0.001, 0.95, 2);
        //初始化种群  染色体长度=50
        Population population = ga.initPopulation(50);
        //评估种群
        ga.evalPopulation(population);

        //设置当前代的追踪
        int generation = 1;

        /**
         * 开始进化循环
         *
         * 每一次的ga问题结束的标准都不同
         * 在此例中，我们知道最完美的解决方案是什么（在其他案例中，我们通常并不知道），
         * 所以我们的 isTerminationConditionMet 方法非常简单：是否存在一个个体其染色体都是1，我们就完成了。
         */
        while(ga.isTerminationConditionMet(population) == false) {
            //打印种群中的最适合个体
            System.out.println("第" + generation + "代最佳解决方案：" + population.getFittest(0).toString());
            System.out.println("    种群使用度=" + population.getPopulationFitness());
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
        System.out.println("    种群使用度=" + population.getPopulationFitness());

    }
}
