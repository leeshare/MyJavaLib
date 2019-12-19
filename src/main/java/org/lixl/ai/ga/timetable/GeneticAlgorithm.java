package org.lixl.ai.ga.timetable;

/**
 * Created by Administrator on 12/19/2019.
 */
public class GeneticAlgorithm {
    private int populationSize;
    private double mutationRate;
    private double crossoverRate;
    private int elitismCount;
    protected int tournamentSize;

    public GeneticAlgorithm(int populationSize, double mutationRate, double crossoverRate,
                            int elitismCount, int tournamentSize) {
        this.populationSize = populationSize;
        this.mutationRate = mutationRate;
        this.crossoverRate = crossoverRate;
        this.elitismCount = elitismCount;
        this.tournamentSize = tournamentSize;
    }

    public Population initPopulation(Timetable timetable) {
        Population population = new Population(this.populationSize, timetable);
        return population;
    }

    public boolean isTerminationConditionMet(int generationCount, int maxGenerations) {
        return (generationCount > maxGenerations);
    }

    public boolean isTerminationConditionMet(Population population) {
        return (population.getFittest(0).getFitness() == 1.0);
    }

    public double calcFitness(Individual individual, Timetable timetable) {
        //从一个已有的课程班复制出一个 新的课程表
        Timetable threadTimetable = new Timetable(timetable);
        threadTimetable.createClasses(individual);

        int clashes = threadTimetable.calcClashes();
        double fitness = 1 / (double) (clashes + 1);    //妙啊！ 除数+1就可能不为0了，还能避免结果产生太大的数

        individual.setFitness(fitness);
        return fitness;
    }

    public void evalPopulation(Population population, Timetable timetable) {
        double populationFitness = 0;

        for(Individual individual : population.getIndividuals()) {
            populationFitness += this.calcFitness(individual, timetable);
        }

        population.setPopulationFitness(populationFitness);
    }

    public Individual selectParent(Population population) {
        Population tournament = new Population(this.tournamentSize);

        population.shuffle();
        for(int i = 0; i < this.tournamentSize; i++) {
            Individual individual = population.getIndividual(i);
            tournament.setIndividual(i, individual);
        }
        return tournament.getFittest(0);
    }

    public Population mutatePopulation(Population population, Timetable timetable) {
        Population newPopulation = new Population(this.populationSize);
        for(int populationIndex = 0; populationIndex < population.size(); populationIndex++) {
            Individual individual = population.getFittest(populationIndex);

            //创建一个随机个体 来用于 交换基因
            Individual randomIndividual = new Individual(timetable);

            //经过测试：放在外面 基本得不到解决方案；而放在里面 50代以内就可以得到最佳解
            //if(populationIndex > this.elitismCount && Math.random() < this.mutateRate) {
            for (int geneIndex = 0; geneIndex < individual.getChromosomeLength(); geneIndex++) {
                    //为什么 这两个判断 要放在 个体遍历基因中呢 ？？？
                    if(populationIndex > this.elitismCount) {
                        if(this.mutationRate > Math.random()){
                            //交换新的基因
                            individual.setGene(geneIndex, randomIndividual.getGene(geneIndex));
                        }
                }
            }

            newPopulation.setIndividual(populationIndex, individual);
        }

        return newPopulation;
    }

    public Population crossoverPopulation(Population population) {
        Population newPopulation = new Population(population.size());

        for(int populationIndex = 0; populationIndex < population.size(); populationIndex++) {
            Individual parent1 = population.getIndividual(populationIndex);

            if(this.crossoverRate > Math.random() && populationIndex > this.elitismCount) {
                Individual offspring = new Individual(parent1.getChromosomeLength());

                Individual parent2 = selectParent(population);

                for(int geneIndex = 0; geneIndex < parent1.getChromosomeLength(); geneIndex++) {
                    if(0.5 > Math.random()) {
                        offspring.setGene(geneIndex, parent1.getGene(geneIndex));
                    } else {
                        offspring.setGene(geneIndex, parent2.getGene(geneIndex));
                    }
                }

                newPopulation.setIndividual(populationIndex, offspring);
            } else {
                newPopulation.setIndividual(populationIndex, parent1);
            }
        }

        return newPopulation;
    }

}
