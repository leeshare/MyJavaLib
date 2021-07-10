package org.lixl.ai.ga.tsp;

/**
 * 这是此 TSP 的主要评估类。
 * 它非常简单——给定一个个体 和 一个标准城市列表，按特定的顺序来访问每个城市从而计算总距离
 * 这个结果从 getDistance() 返回，用于 GeneticAlgorithm.calcFitness() 方法中
 * Created by Administrator on 12/16/2019.
 */
public class Route {
    private City route[];
    private double distance = 0;

    public Route(Individual individual, City cities[]) {
        int chromosome[] = individual.getChromosome();
        this.route = new City[cities.length];
        for (int geneIndex = 0; geneIndex < chromosome.length; geneIndex++) {
            this.route[geneIndex] = cities[chromosome[geneIndex]];
        }
    }

    /**
     * Get route distance
     *
     * @return
     */
    public double getDistance() {
        if (this.distance > 0) {
            return this.distance;
        }

        double totalDistance = 0;
        for (int cityIndex = 0; cityIndex + 1 < this.route.length; cityIndex++) {
            totalDistance += this.route[cityIndex].distanceFrom(this.route[cityIndex + 1]);
        }

        //最后一个城市 到 第一个城市 的距离
        totalDistance += this.route[this.route.length - 1].distanceFrom(this.route[0]);
        this.distance = totalDistance;

        return totalDistance;
    }
}
