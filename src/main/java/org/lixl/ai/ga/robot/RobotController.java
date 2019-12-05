package org.lixl.ai.ga.robot;

/**
 *
 * Created by lxl on 19/12/1.
 */
public class RobotController {
    public static int maxGenerations = 1000;


    public static void main(String[] args) {

        /**
         *
         * As a reminder:
         *  0 = Empty
         *  1 = Wall
         *  2 = Starting position
         *  3 = Route
         *  4 = Goal position
         */
        Maze maze = new Maze(new int[][] {
            {0, 0, 0, 0, 1, 0, 1, 3, 2},
            {1, 0, 1, 1, 1, 0, 1, 3, 1},
            {1, 0, 0, 1, 3, 3, 3, 3, 1},
            {3, 3, 3, 1, 3, 1, 1, 0, 1},
            {3, 1, 3, 3, 3, 1, 1, 0, 0},
            {3, 3, 1, 1, 1, 1, 0, 1, 1},
            {1, 3, 0, 1, 3, 3, 3, 3, 3},
            {0, 3, 1, 1, 3, 1, 0, 1, 3},
            {1, 3, 3, 3, 3, 1, 1, 1, 4}
        });

        //创建遗传算法
        //种群数 200，变异率 0.05，交叉率 0.9，精英数 2，锦标赛个数 10
        GeneticAlgorithm ga = new GeneticAlgorithm(200, 0.05, 0.9, 2, 10);
        Population population = ga.initPopulation(128);
        ga.evalPopulation(population, maze);
        //保持跟踪当前代
        int generation = 1;
        //开始进化循环 evolution
        while(ga.isTerminationConditionMet(generation, maxGenerations) == false) {
            //打印种群的适应度
            Individual fittest = population.getFittest(0);
            System.out.println("G" + generation + " 最佳解决（" + fittest.getFitness() + "）: " + fittest.toString());

            //应用交叉
            population = ga.crossoverPopulation(population);

            //应用突变
            population = ga.mutatePopulation(population);

            //评估种群
            ga.evalPopulation(population, maze);

            //增加当前代
            generation++;
        }

        System.out.println("在第" + maxGenerations + "代后停止");
        Individual fittest = population.getFittest(0);
        System.out.println("最佳解决（" + fittest.getFitness() + "）: " + fittest.toString());
        System.out.println(fittest.toStringZn());

    }
}
