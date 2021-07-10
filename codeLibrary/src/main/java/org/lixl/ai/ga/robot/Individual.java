package org.lixl.ai.ga.robot;

/**
 * Created by lxl on 19/12/1.
 */
public class Individual {
    //染色体
    private int[] chromosome;
    //适应度
    private double fitness = -1;

    public Individual(int[] chromosome) {
        this.chromosome = chromosome;
    }

    public Individual(int chromosomeLength) {
        this.chromosome = new int[chromosomeLength];
        for (int gene = 0; gene < chromosomeLength; gene++) {
            if (0.5 < Math.random()) {
                this.setGene(gene, 1);
            } else {
                this.setGene(gene, 0);
            }
        }
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
        for (int gene = 0; gene < this.chromosome.length; gene++) {
            output += this.chromosome[gene];
        }
        return output;
    }

    public String toStringZn() {
        String output = "";
        for (int gene = 0; gene < this.chromosome.length; gene += 2) {
            String c = this.chromosome[gene] + "" + this.chromosome[gene + 1];
            switch (c) {
                case "00":
                    output += "停止 ";
                    break;
                case "01":
                    output += "前进 ";
                    break;
                case "10":
                    output += "左转 ";
                    break;
                case "11":
                    output += "右转 ";
                    break;
            }
        }
        return output;
    }

}
