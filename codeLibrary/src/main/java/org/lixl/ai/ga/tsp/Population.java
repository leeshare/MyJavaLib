package org.lixl.ai.ga.tsp;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

/**
 * Created by Administrator on 12/16/2019.
 */
public class Population {
    private Individual population[];
    private double populationFitness = -1;

    /**
     * 初始化空白种群
     * @param populationSize
     */
    public Population(int populationSize) {
        this.population = new Individual[populationSize];
    }

    /**
     * 初始化种群
     * @param populationSize  种群规模
     * @param chromosomeLength  种群每个个体的染色体长度
     */
    public Population(int populationSize, int chromosomeLength) {
        this.population = new Individual[populationSize];

        for(int individualCount = 0; individualCount < populationSize; individualCount++) {
            Individual individual = new Individual(chromosomeLength);
            this.population[individualCount] = individual;
        }
    }

    public Individual[] getIndividuals() {
        return this.population;
    }

    /**
     * 寻找 第几强壮 的个体
     *      比如 0 就是最强壮的，其他依次类推
     * @param offset
     * @return
     */
    public Individual getFittest(int offset) {
        //按适应度 把种群排序
        Arrays.sort(this.population, new Comparator<Individual>() {
            @Override
            public int compare(Individual o1, Individual o2) {
                if(o1.getFitness() > o2.getFitness()) {
                    return -1;
                } else if(o1.getFitness() < o2.getFitness()) {
                    return 1;
                }
                return 0;
            }
        });

        return this.population[offset];
    }

    public void setPopulationFitness(double fitness) {
        this.populationFitness = fitness;
    }

    public double getPopulationFitness() {
        return this.populationFitness;
    }

    public int size() {
        return this.population.length;
    }

    public Individual setIndividual(int offset, Individual individual) {
        return population[offset] = individual;
    }

    public Individual getIndividual(int offset) {
        return population[offset];
    }

    public void shuffle() {
        Random random = new Random();
        for(int i = population.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            Individual a = population[index];
            population[index] = population[i];
            population[i] = a;
        }
    }

}
