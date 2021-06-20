package org.lixl.ai.ga.tsp;

/**
 * Created by Administrator on 12/16/2019.
 */
public class Individual {
    private int[] chromosome;
    private double fitness = -1;

    public Individual(int[] chromosome) {
        this.chromosome = chromosome;
    }

    public Individual(int chromosomeLength) {
        int[] individual;
        individual = new int[chromosomeLength];

        /**
         * 在这个例子中，我们不再需要简单设置二进制的 0s 或 1s ———— 我们需要使用每个城市的索引作为每个基因
         * 我们也不再需要随机(randomize)或洗牌(shuffle)这个染色体，
         * as crossover and mutation will untimately take care of that for us.
         */
        for(int gene = 0; gene < chromosomeLength; gene++) {
            individual[gene] = gene;
        }

        this.chromosome = individual;
    }

    public int[] getChromosome() {
        return this.chromosome;
    }

    public int getChromosomeLength() {
        return this.chromosome.length;
    }

    public void setGene(int offset, int gene) {
        this.chromosome[offset] = gene;
    }

    public int getGene(int offset) {
        return this.chromosome[offset];
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public double getFitness() {
        return this.fitness;
    }

    public String toString() {
        String output = "";
        for(int gene = 0; gene < this.chromosome.length; gene++) {
            output += this.chromosome[gene] + ",";
        }
        return output;
    }

    /**
     * 在这个个体中 寻找一个特定的基因
     *
     * 用于距离，在一个旅行商问题中，哪个城市已经编码了，就用一个数字(0-99范围)表示，这个方法将检查比如城市“42”已存在
     * @param gene
     * @return
     */
    public boolean containsGene(int gene) {
        for(int i = 0; i < this.chromosome.length; i++) {
            if(this.chromosome[i] == gene) {
                return true;
            }
        }
        return false;
    }

}
