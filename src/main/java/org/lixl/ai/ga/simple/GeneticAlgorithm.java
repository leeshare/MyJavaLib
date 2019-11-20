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

    /**
     * 检查种群是否符合结束条件，就可以终止进化了
     * @param population
     * @return
     */
    public boolean isTerminationConditionMet(Population population) {
        for(Individual individual : population.getIndividuals()) {
            if(individual.getFitness() == 1) {
                return true;
            }
        }
        return false;
    }

    /**
     * 为交叉选择父母
     * @param population
     * @return
     */
    public Individual selectParent(Population population) {
        //获取个体
        Individual[] individuals = population.getIndividuals();

        double populationFitness = population.getPopulationFitness();
        double rouletteWheelPosition = Math.random() * populationFitness;

        double spinWheel = 0;
        for(Individual individual : individuals) {
            spinWheel += individual.getFitness();
            if(spinWheel >= rouletteWheelPosition) {
                return individual;
            }
        }
        return individuals[population.size() - 1];
    }

    /**
     * 对种群应用交叉
     * @param population
     * @return
     */
    public Population crossoverPopulation(Population population) {
        //建立新的种群
        Population newPopulation = new Population(population.size());

        //按适应度来循环当前种群
        for(int populationIndex = 0; populationIndex < population.size(); populationIndex++) {
            Individual parent1 = population.getFittest(populationIndex);
            //对种群个体是否要应用交叉？
            if(this.crossoverRate > Math.random() && populationIndex >= this.elitismCount) {
                //初始化后代
                Individual offspring = new Individual(parent1.getChromosomeLength());
                //找到第二个父母
                Individual parent2 = selectParent(population);

                //循环基因组
                for(int geneIndex = 0; geneIndex < parent1.getChromosomeLength(); geneIndex++) {
                    //使用parent1一半的基因 和 parent2一半的基因
                    if(0.5 > Math.random()) {
                        offspring.setGene(geneIndex, parent1.getGene(geneIndex));
                    } else {
                        offspring.setGene(geneIndex, parent2.getGene(geneIndex));
                    }
                }

                //添加后代到新种群中
                newPopulation.setIndividual(populationIndex, offspring);
            } else {
                //添加没有使用交叉的个体到新种群中
                newPopulation.setIndividual(populationIndex, parent1);
            }
        }

        return newPopulation;
    }

    /**
     * 对种群应用变异
     * @param population
     * @return
     */
    public Population mutatePopulation(Population population) {

        Population newPopulation = new Population(this.populationSize);

        for(int populationIndex = 0; populationIndex < population.size(); populationIndex++) {
            Individual individual = population.getFittest(populationIndex);

            for(int geneIndex = 0; geneIndex < individual.getChromosomeLength(); geneIndex++) {
                if(populationIndex > this.elitismCount) {
                    if(this.mutationRate > Math.random()) {
                        int newGene = 1;
                        if(individual.getGene(geneIndex) == 1) {
                            newGene = 0;
                        }
                        individual.setGene(geneIndex, newGene);
                    }
                }
            }

            newPopulation.setIndividual(populationIndex, individual);
        }

        return newPopulation;
    }

}
