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
     *
     * @param population
     * @param maze
     */
    public void evalPopulation(Population population, Maze maze) {
        double populationFitness = 0;

        for (Individual individual : population.getIndividuals()) {
            populationFitness += this.calcFitness(individual, maze);
        }

        population.setPopulationFitness(populationFitness);
    }

    public boolean isTerminationConditionMet(int generationsCount, int maxGenerations) {
        return (generationsCount > maxGenerations);
    }

    /**
     * 对用于交叉的个体选择一个父母，使用锦标赛选择
     * 比如将锦标赛参赛选手 tournamentSize = 10
     * 那么每次从种群中取10个参赛
     * 这里最终从锦标赛个体中取最好的一个
     *
     * @param population
     * @return
     */
    public Individual selectParent(Population population) {
        //Create tournament  创建一个空的种群，用于竞标赛
        Population tournament = new Population(this.tournamentSize);

        //添加随机个体到竞标赛种群中
        //把传入的种群（即 原种群） 洗牌
        population.shuffle();
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
     * 基因突变
     *
     * @param population
     * @return
     */
    public Population mutatePopulation(Population population) {
        //初始化新种群
        Population newPopulation = new Population(this.populationSize);

        //按适应度循环当前种群
        for (int populationIndex = 0; populationIndex < population.size(); populationIndex++) {
            Individual individual = population.getFittest(populationIndex);
            //这一步，不仅得到这个个体，还将种群按适应度做了排序

            //循环个体的基因
            for (int geneIndex = 0; geneIndex < individual.getChromosomeLength(); geneIndex++) {
                //跳过突变：如果这是个精英  elite
                if (populationIndex >= this.elitismCount) {
                    //这个基因是否需要突变
                    if (this.mutationRate > Math.random()) {
                        //get new gene
                        int newGene = 1;
                        if (individual.getGene(geneIndex) == 1) {
                            newGene = 0;
                        }
                        //突变基因
                        individual.setGene(geneIndex, newGene);
                    }
                }

            }

            //添加个体到种群
            newPopulation.setIndividual(populationIndex, individual);
        }

        //返回突变后的种群
        return newPopulation;
    }

    /**
     * 交叉种群使用单点交叉       using single point crossover
     * <p>
     * 单点交叉 不同于 simple中的交叉 （simple中的交叉是用一个随机数和 0.5 比较，然后来选择用 parent1 还是 parent2）
     * 比如
     * Parent1 : AAAAAAAAAA
     * Parent2 : BBBBBBBBBB
     * sample中 Child : AABBAABABA
     * 而这里的 Child : AAAABBBBBB
     *
     * @param population
     * @return
     */
    public Population crossoverPopulation(Population population) {
        //创建一个新的种群
        Population newPopulation = new Population(population.size());

        //按适应度循环当前种群
        for (int populationIndex = 0; populationIndex < population.size(); populationIndex++) {
            Individual parent1 = population.getFittest(populationIndex);

            //这个个体是否接受交叉
            if (this.crossoveRate > Math.random() && populationIndex >= this.elitismCount) {
                //初始化后代
                Individual offspring = new Individual(parent1.getChromosomeLength());

                //找到第二位父母
                Individual parent2 = this.selectParent(population);

                //获得随机交换的点 get random swap point
                int swapPoint = (int) (Math.random() * (parent1.getChromosomeLength() + 1));

                //循环基因组
                for (int geneIndex = 0; geneIndex < parent1.getChromosomeLength(); geneIndex++) {
                    //使用一半的 parent1的基因 和一半的 parent2的基因
                    if (geneIndex < swapPoint) {
                        offspring.setGene(geneIndex, parent1.getGene(geneIndex));
                    } else {
                        offspring.setGene(geneIndex, parent2.getGene(geneIndex));
                    }
                }

                //添加这个后代到新种群中
                newPopulation.setIndividual(populationIndex, offspring);
            } else {
                newPopulation.setIndividual(populationIndex, parent1);
            }
        }

        return newPopulation;
    }

}
