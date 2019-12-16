package org.lixl.ai.ga.allone;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

/**
 * 自然遗传中的 种群
 * Created by Administrator on 11/15/2019.
 */
public class Population {
    private Individual population[];
    private double populationFitness = -1;  //种群适应度 0-总群个体数，为种群每个个体适应度之和

    /**
     * 初始化 空白种群
     * @param populationSize
     */
    public Population(int populationSize) {
        this.population = new Individual[populationSize];
    }

    /**
     * 初始化 种群
     * @param populationSize
     * @param chromosomeLength
     */
    public Population(int populationSize, int chromosomeLength) {
        //初始化种群 为一个 个体的数组
        this.population = new Individual[populationSize];

        //创建每个个体
        for(int individualCount = 0; individualCount < populationSize; individualCount++) {
            //创建一个个体，初始化其染色体为给定值
            Individual individual = new Individual(chromosomeLength);
            //添加此个体到种群中
            this.population[individualCount] = individual;
        }

    }

    /**
     * 获取种群的个体数组
     * @return
     */
    public Individual[] getIndividuals() {
        return this.population;
    }

    /**
     * 按适应度 在种群中 寻找一个个体
     * @param offset
     *      你想要的个体的偏移量
     *      0 是最强壮的； 种群长度-1 是最弱的。
     * @return
     */
    public Individual getFittest(int offset) {
        //按适应度 排序 种群
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
        //返回最适应的个体
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

    /**
     * 在偏移量处 设置个体
     * @param offset
     * @param individual
     * @return
     */
    public Individual setIndividual(int offset, Individual individual) {
        return population[offset] = individual;
    }

    /**
     * 获取偏移量处的个体
     * @param offset
     * @return
     */
    public Individual getIndividual(int offset) {
        return population[offset];
    }

    /**
     * 将种群 原地洗牌
     */
    public void shuffle() {
        Random random = new Random();
        for(int i = population.length - 1; i > 0; i--) {
            int index = random.nextInt(i+1);
            Individual a = population[index];
            population[index] = population[i];
            population[i] = a;
        }
    }

}
