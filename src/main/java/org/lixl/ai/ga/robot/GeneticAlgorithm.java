package org.lixl.ai.ga.robot;

/**
 * Created by lxl on 19/12/1.
 */
public class GeneticAlgorithm {
    private int populationSize;
    private double mutationRate;
    private double crossoveRate;
    private int elitismCount;

    //新属性:种群的规模在交叉时使用"锦标赛选择"    tournament selection
    protected int tournamentSize;

    public GeneticAlgorithm(int populationSize, double mutationRate, double crossoverRate, int elitismCount, int tournamentSize) {
        this.populationSize = populationSize;
        this.mutationRate = mutationRate;
        this.crossoveRate = crossoverRate;
        this.elitismCount = elitismCount;
        this.tournamentSize = tournamentSize;
    }

    public Population initPopulation(int chromosomeLength) {
        Population population = new Population(this.populationSize, chromosomeLength);
        return population;
    }

    public double calcFitness(Individual individual, Maze maze) {
        int[] chromosome = individual.getChromosome();

        //获取适应度
        Robot robot = new Robot(chromosome, maze, 100);
        robot.run();
        int fitness = maze.scoreRoute(robot.getRoute());

        //Score fitness
        individual.setFitness(fitness);

        return fitness;
    }

    /**
     * 评估整个种群
     * @param population
     * @param maze
     */
    public void evalPopulation(Population population, Maze maze) {
        double populationFitness = 0;

        for(Individual individual : population.getIndividuals()) {
            populationFitness += this.calcFitness(individual, maze);
        }

        population.setPopulationFitness(populationFitness);
    }

    public boolean isTerminationConditionMet(int generationsCount, int maxGenerations) {
        return (generationsCount > maxGenerations);
    }

    /**
     * 对用于交叉的个体选择一个父母，使用锦标赛选择
     *   比如将锦标赛参赛选手 tournamentSize = 10
     *   那么每次从种群中取10个参赛
     * 这里最终从锦标赛个体中取最好的一个
     * @param population
     * @return
     */
    public Individual selectParent(Population population) {
        //Create tournament  创建竞标赛
        Population tournament = new Population(this.tournamentSize);

        //添加随机个体到竞标赛中
        population.shuffle();
        for(int i = 0; i < this.tournamentSize; i++) {
            Individual tournamentIndividual = population.getIndividual(i);
            tournament.setIndividual(i, tournamentIndividual);
        }

        //返回最好的
        return tournament.getFittest(0);
    }

    /**
     * 基因突变
     * @param population
     * @return
     */
    public Population mutatePopulation(Population population) {
        //初始化新种群
        Population newPopulation = new Population(this.populationSize);

    }

}
