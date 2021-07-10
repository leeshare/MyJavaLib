package org.lixl.ai.ga.tsp;

import java.util.Arrays;

/**
 * Created by Administrator on 12/16/2019.
 */
public class GeneticAlgorithm {
    private int populationSize;
    private double mutationRate;
    private double crossoverRate;
    protected int elitismCount;
    protected int tournamentSize;

    public GeneticAlgorithm(int populationSize, double mutationRate, double crossoverRate, int elitismCount, int tournamentSize) {
        this.populationSize = populationSize;
        this.mutationRate = mutationRate;
        this.crossoverRate = crossoverRate;
        this.elitismCount = elitismCount;
        this.tournamentSize = tournamentSize;
    }

    public Population initPopulation(int chromosomeLength) {
        Population population = new Population(this.populationSize, chromosomeLength);
        return population;
    }

    /**
     * 检查种群已达到最终条件————即检查我们已超过(exceed  prevail transcend)允许的代数
     *
     * @param generationsCount
     * @param maxGenerations
     * @return
     */
    public boolean isTerminationConditionMet(int generationsCount, int maxGenerations) {
        return generationsCount > maxGenerations;
    }

    /**
     * 计算个体的适应度的值
     * <p>
     * 适应度，在这个问题中是 路径总距离的反比例(inversely 相反地 proportional 比例的)
     * 总距离的计算 位于 Route 类
     *
     * @param individual
     * @param cities
     * @return
     */
    public double calcFitness(Individual individual, City cities[]) {
        //将个体染色体中的基因顺序，作为城市的顺序，保存在 route 中的 route[] 数组中
        Route route = new Route(individual, cities);
        //计算总距离
        //用 1 除 是为了和以前计算适应度统一，值越大，则越强壮
        double fitness = 1 / route.getDistance();

        individual.setFitness(fitness);

        return fitness;
    }

    /**
     * 评估种群 ————在每个个体上执行 calcFitness
     * evaluate / assess
     *
     * @param population
     * @param cities
     */
    public void evalPopulation(Population population, City cities[]) {
        double populationFitness = 0;

        for (Individual individual : population.getIndividuals()) {
            populationFitness += this.calcFitness(individual, cities);
        }

        double avgFitness = populationFitness / population.size();
        population.setPopulationFitness(avgFitness);
    }

    /**
     * 选择父母，用于交叉
     * 仍使用 锦标赛选择
     *
     * @param population
     * @return
     */
    public Individual selectParent(Population population) {
        //创建用于锦标赛的种群（此时个体都是空的）
        Population tournament = new Population(this.tournamentSize);

        //把传入的种群（即 原种群） 洗牌
        population.shuffle();
        // 填充竞标赛种群的每个个体
        for (int i = 0; i < this.tournamentSize; i++) {
            //由于原种群已洗牌，所以按顺序取，其实也是随机的
            Individual tournamentIndividual = population.getIndividual(i);
            //将原种群随便取的个体 填入 锦标赛种群中
            tournament.setIndividual(i, tournamentIndividual);
        }

        //返回竞标赛种群中 最强壮的
        return tournament.getFittest(0);
    }

    /**
     * 对种群使用交叉
     * <p>
     * 在TSP中的染色体 要求每个城市只能被访问一次
     * 均衡的(uniform)交叉可以打破染色体 被意外的选中一个城市已被访问过了
     * 这就会导致有的城市被访问多次而有的城市从未访问过
     * <p>
     * 此外，均衡或随机交叉都不能保存最重要的遗传信息方向(aspect)：一组成是的特定的顺序
     * <p>
     * 我们需要更聪明的交叉算法：我们需要做的是选择两个中心点，
     * 从一个父母中添加染色体到 其中一个范围，
     * 然后仅填未保存的城市到第二个范围。
     * 这样就可以确保没有城市被跳过或访问多次，同时也保存着城市的顺序。
     *
     * @param population
     * @return
     */
    public Population crossoverPopulation(Population population) {
        Population newPopulation = new Population(population.size());

        for (int populationIndex = 0; populationIndex < population.size(); populationIndex++) {
            Individual parent1 = population.getFittest(populationIndex);

            //在设定的 交叉率范围内   且 非精英个体（因为精英肯定是最前面几个）
            if (this.crossoverRate > Math.random() && populationIndex >= this.elitismCount) {
                Individual parent2 = this.selectParent(population);

                //初始化 后代
                int offspringChromosome[] = new int[parent1.getChromosomeLength()];
                Arrays.fill(offspringChromosome, -1);
                Individual offspring = new Individual(offspringChromosome);

                //在整个染色体长度 随机取2个点
                int substrPos1 = (int) (Math.random() * parent1.getChromosomeLength());
                int substrPos2 = (int) (Math.random() * parent1.getChromosomeLength());

                //使 小点的 为起始点，大点的 为截止点
                final int startSubstr = Math.min(substrPos1, substrPos2);
                final int endSubstr = Math.max(substrPos1, substrPos2);

                //在 开始到截止的范围内 用 第一个父母的基因填充
                for (int i = startSubstr; i < endSubstr; i++) {
                    offspring.setGene(i, parent1.getGene(i));
                }

                for (int i = 0; i < parent2.getChromosomeLength(); i++) {
                    int parent2Gene = i + endSubstr;
                    //比如 两个点是  10 ～ 20
                    //当index循环到 80时， 80+20 >= 100，则 parent2Gene = 100 - 100 = 0，就到了第0个位置
                    if (parent2Gene >= parent2.getChromosomeLength()) {
                        parent2Gene -= parent2.getChromosomeLength();
                    }

                    if (offspring.containsGene(parent2.getGene(parent2Gene)) == false) {
                        for (int ii = 0; ii < offspring.getChromosomeLength(); ii++) {
                            if (offspring.getGene(ii) == -1) {
                                offspring.setGene(ii, parent2.getGene(parent2Gene));
                                break;
                            }
                        }
                    }
                }

                // Add child
                newPopulation.setIndividual(populationIndex, offspring);
            } else {
                //
                newPopulation.setIndividual(populationIndex, parent1);
            }
        }

        return newPopulation;
    }

    /**
     * 对种群使用突变
     * <p>
     * 由于TSP必须只能访问一次每个城市，所以这种突变将随机交换两个基因
     * 而不是想以前的例子随机二进制翻转(flip)一个基因  （以前是 0 -> 1, 1 -> 0 这样的翻转）
     *
     * @param population
     * @return
     */
    public Population mutatePopulation(Population population) {
        //初始化一个新的种群
        Population newPopulation = new Population(this.populationSize);

        //按适应度 循环当前种群
        for (int populationIndex = 0; populationIndex < population.size(); populationIndex++) {
            Individual individual = population.getFittest(populationIndex);

            //非精英个体才执行突变
            if (populationIndex >= this.elitismCount) {
                for (int geneIndex = 0; geneIndex < individual.getChromosomeLength(); geneIndex++) {
                    //大于突变率的才执行突变
                    if (this.mutationRate > Math.random()) {
                        int newGenePos = (int) (Math.random() * individual.getChromosomeLength());

                        //每次突变 只交换一对基因
                        int gene1 = individual.getGene(newGenePos);
                        int gene2 = individual.getGene(geneIndex);

                        individual.setGene(geneIndex, gene1);
                        individual.setGene(newGenePos, gene2);
                    }
                }
            }

            //
            newPopulation.setIndividual(populationIndex, individual);
        }

        //
        return newPopulation;
    }

}
