package org.lixl.ai.ga.timetable;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

/**
 * Created by Administrator on 12/18/2019.
 */
public class Population {
    private Individual population[];
    private double populationFitness = -1;

    public Population(int populationSize) {
        this.population = new Individual[populationSize];
    }

    public Population(int populationSize, Timetable timetable) {
        this.population = new Individual[populationSize];

        for(int i = 0; i < populationSize; i++) {
            Individual individual = new Individual(timetable);
            this.population[i] = individual;
        }
    }

    public Population(int populationSize, int chromosomeLength) {
        this.population = new Individual[populationSize];

        for(int i = 0; i < populationSize; i++) {
            Individual individual = new Individual(chromosomeLength);
            this.population[i] = individual;
        }
    }

    public Individual[] getIndividuals() {
        return this.population;
    }

    public Individual getFittest(int offset) {
        Arrays.sort(this.population, new Comparator<Individual>() {
            @Override
            public int compare(Individual o1, Individual o2) {
                if(o1.getFitness() > o2.getFitness()){
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
        //for(int i = 0; i < population.length - 1; i++) {
        for(int i = population.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);  //生成 0 ～ i + 1 之间的一个随机数
            Individual temp = population[index];
            population[index] = population[i];
            population[i] = temp;
        }
    }
}
