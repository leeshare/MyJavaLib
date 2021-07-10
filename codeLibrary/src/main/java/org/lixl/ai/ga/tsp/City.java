package org.lixl.ai.ga.tsp;

/**
 * Created by Administrator on 12/16/2019.
 */
public class City {
    private int x;
    private int y;

    public City(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * 计算到另一个城市的距离
     *
     * @param city
     * @return
     */
    public double distanceFrom(City city) {
        double deltaXSq = Math.pow(city.getX() - this.getX(), 2);
        double deltaYSq = Math.pow(city.getY() - this.getY(), 2);

        double distance = Math.sqrt(Math.abs(deltaXSq + deltaYSq));
        return distance;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }
}
