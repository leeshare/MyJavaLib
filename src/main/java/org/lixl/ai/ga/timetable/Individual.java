package org.lixl.ai.ga.timetable;

import org.lixl.ai.ga.timetable.attr.Group;
import org.lixl.ai.ga.timetable.attr.Module;

/**
 * Created by Administrator on 12/16/2019.
 */
public class Individual {
    private int[] chromosome;
    private double fitness = -1;

    /**
     * 基于一个课程表 来初始化随机个体
     *
     * 这个课程表类是有点超载。它既知道混合信息（课程必须排，教授必须教，教室必须存在）
     *      --也懂得如何解包 包含变量信息的染色体（哪个教授在何时教哪个班）
     *
     *  在这个案例中，我们使用课程表仅用于混合信息，并生成一个随机染色体，making guesses at hte variable information.
     *
     *  用课程表所给的混合信息，我们创建一个染色体随机分给时间段、教室和教授，再将此染色体对应到学生组和课程上。
     *
     * @param timetable
     */
    public Individual(Timetable timetable) {
        int numClasses = timetable.getNumClasses();

        // 1 基因为 教室，1 基因为 时间，1基因为教授
        int chromosomeLength = numClasses * 3;
        //创建随机个体
        int[] newChromosome = new int[chromosomeLength];
        int chromosomeIndex = 0;
        //循环学生组
        for(Group group : timetable.getGroupsAsArray()) {
            //循环课程
            for(int moduleId : group.getModuleIds()) {
                //添加随机时间
                int timeslotId = timetable.getRandomTimeslot().getTimeslotId();
                newChromosome[chromosomeIndex] = timeslotId;
                chromosomeIndex++;
                //添加随机教室
                int roomId = timetable.getRandomRoom().getRoomId();
                newChromosome[chromosomeIndex] = roomId;
                chromosomeIndex++;
                //添加随机教授
                Module module = timetable.getModule(moduleId);
                newChromosome[chromosomeIndex] = module.getRandomProfessorId();
                chromosomeIndex++;
            }
        }

        this.chromosome = newChromosome;

    }

    public Individual(int chromosomeLength) {
        int[] individual;
        individual = new int[chromosomeLength];

        for (int gene = 0; gene < chromosomeLength; gene++) {
            individual[gene] = gene;
        }

        this.chromosome = individual;
    }

    public Individual(int[] chromosome) {
        this.chromosome = chromosome;
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

    public boolean containsGene(int gene) {
        for(int i = 0; i < this.chromosome.length; i++) {
            if(this.chromosome[i] == gene) {
                return true;
            }
        }
        return false;
    }

}
