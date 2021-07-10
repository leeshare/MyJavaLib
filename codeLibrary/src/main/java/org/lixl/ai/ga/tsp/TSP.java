package org.lixl.ai.ga.tsp;

/**
 * 主要的： 旅行商问题（Traveling Salesman Problem） 执行类
 * 我们没有一个实际的城市列表，所以我们随机生成一个 100x100的地图
 * <p>
 * 这个TSP要求每个城市必须且仅被访问一次，所有我们必须小心，当我们初始化一个随机个体、交叉或变异时。
 * 检查 GeneticAlgorithm类用于实现交叉和编译的部分。
 * <p>
 * Created by Administrator on 12/16/2019.
 */
public class TSP {
    /**
     * 最多执行次数 或叫种群繁衍后代的次数
     */
    public static int maxGenerations = 10000;

    public static void main(String[] args) {

        //City cities[] = someRandomCities();
        City cities[] = sevenCities();

        GeneticAlgorithm ga = new GeneticAlgorithm(100, 0.001, 0.9, 2, 5);

        Population population = ga.initPopulation(cities.length);

        ga.evalPopulation(population, cities);

        Route startRoute = new Route(population.getFittest(0), cities);
        System.out.println("Start Distance: " + startRoute.getDistance());

        int generation = 1;
        while (ga.isTerminationConditionMet(generation, maxGenerations) == false) {
            Route route = new Route(population.getFittest(0), cities);
            System.out.println("G" + generation + " Best distance: " + route.getDistance());

            population = ga.crossoverPopulation(population);

            population = ga.mutatePopulation(population);

            ga.evalPopulation(population, cities);

            generation++;
        }

        System.out.println("Stopped after " + maxGenerations + " generations.");
        Route route = new Route(population.getFittest(0), cities);
        System.out.println("Best distance: " + route.getDistance());
    }

    private static City[] someRandomCities() {
        int numCities = 100;
        City cities[] = new City[numCities];
        //循环创建随机城市
        for (int cityIndex = 0; cityIndex < numCities; cityIndex++) {
            int xPos = (int) (100 * Math.random());
            int yPos = (int) (100 * Math.random());

            cities[cityIndex] = new City(xPos, yPos);
        }

        Boolean isExist = false;
        for (int i = 0; i < 100; i++) {
            for (int j = 99; j >= 0; j--) {
                if (i != j && cities[i].getX() == cities[j].getX() && cities[i].getY() == cities[j].getY()) {
                    System.out.println("第" + i + "和第" + j + "城市重合： (" + cities[i].getX() + "," + cities[i].getY() + ")");
                    isExist = true;
                }
            }
        }

        if (!isExist) {
            //System.out.println("无重合城市");
            //return;
        }

        return cities;
    }

    private static City[] sevenCities() {
        maxGenerations = 100;
        City[] cities = new City[7];
        cities[0] = new City(0, 0);
        cities[1] = new City(1, 6);
        cities[2] = new City(2, 3);
        cities[3] = new City(5, 2);
        cities[4] = new City(6, 5);
        cities[5] = new City(7, 1);
        cities[6] = new City(8, 4);

        return cities;
    }
}
