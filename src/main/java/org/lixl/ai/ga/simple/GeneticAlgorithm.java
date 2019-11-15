package org.lixl.ai.ga.simple;

/**
 * Created by Administrator on 11/15/2019.
 */
public class GeneticAlgorithm {
    private int populationSize;

    /**
     * 变异率是 小概率
     * 将随机变异在给定的代中。
     * 比率范围是0.0-1.0，但一般很小（小于0.1或更小）
     */
    private double mutationRate;

    /**
     * 交叉率是 小概率，是两个个体彼此交叉，分享遗传信息，
     * 且使用彼此父母的特性来创建后代
     * 比率同 mutation rate 范围是0.0-1.0 或更小
     */
    private double crossoverRate;

    /**
     * 精英是 种群中最强壮的个体
     * 应该被保护从种群代代中
     * 如果一个个体是精英，它将不会变异或交叉
     */
    private int elitismCount;

    public GeneticAlgorithm(int populationSize, double mutationRate, double crossoverRate, int elitismCount) {
        this.populationSize = populationSize;
        this.mutationRate = mutationRate;
        this.crossoverRate = crossoverRate;
        this.elitismCount = elitismCount;
    }

    /**
     * 初始化种群
     * @param chromosomeLength
     * @return
     */
    public Population initPopulation(int chromosomeLength) {
        Population population = new Population(this.populationSize, chromosomeLength);
        return population;
    }

    /**
     * 计算个体的适应度
     * 在此案例中，适应度分值很简单：染色体中的数值
     * @param individual
     * @return
     */
    public double calcFitness(Individual individual) {
        //跟踪正确代的成员
        int correntGenes = 0;

        //循环个体的基因
        for(int geneIndex = 0; geneIndex < individual.getChromosomeLength(); geneIndex++) {
            //为每个找到的“1”添加一个适应度点
            if(individual.getGene(geneIndex) == 1) {
                correntGenes += 1;
            }
        }

        double fitness = (double) correntGenes / individual.getChromosomeLength();

        //存储适应度
        individual.setFitness(fitness);

        return fitness;
    }

    /**
     * 进化整个种群
     *
     * @param population
     */
    public void evalPopulation(Population population) {
        double populationFitness = 0;

        for(Individual individual : population.getIndividuals()) {
            populationFitness += calcFitness(individual);
        }

        population.setPopulationFitness(populationFitness);
    }


}
