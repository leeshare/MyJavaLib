package org.lixl.ai.ga.allones;

/**
 * 自然遗传中的 个体
 * Created by Administrator on 11/15/2019.
 */
public class Individual {
    private int[] chromosome;       //染色体
    private double fitness = -1;    //适应度，0～1 值越大，表明越强壮，也就越适应

    /**
     *
     * 使用指定的染色体来初始化个体
     * @param chromosome
     */
    public Individual(int[] chromosome) {
        this.chromosome = chromosome;
    }

    /**
     * 初始化一个随机个体
     * @param chromosomeLength
     */
    public Individual(int chromosomeLength) {
        this.chromosome = new int[chromosomeLength];
        for(int gene = 0; gene < chromosomeLength; gene++){
            if(0.5 < Math.random())
                this.setGene(gene, 1);
            else
                this.setGene(gene, 0);
        }
    }

    /**
     * 获取个体的染色体
     * @return
     */
    public int[] getChromosome(){
        return this.chromosome;
    }

    public int getChromosomeLength(){
        return this.chromosome.length;
    }

    /**
     * 在偏移量处 设置 基因
     * @param offset
     * @param gene
     */
    public void setGene(int offset, int gene) {
        this.chromosome[offset] = gene;
    }

    /**
     * 在偏移量处 获取 基因
     * @param offset
     * @return
     */
    public int getGene(int offset) {
        return this.chromosome[offset];
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public double getFitness() {
        return  this.fitness;
    }

    public String toString() {
        String output = "";
        for(int gene = 0; gene < this.chromosome.length; gene++) {
            output += this.chromosome[gene];
        }
        return output;
    }

}
