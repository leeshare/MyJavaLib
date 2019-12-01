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

        Robot robot = new Robot(chromosome, maze, 100);
    }

}
