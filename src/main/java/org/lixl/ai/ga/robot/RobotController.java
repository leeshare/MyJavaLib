package org.lixl.ai.ga.robot;

/**
 *
 * Created by lxl on 19/12/1.
 */
public class RobotController {
    public static int maxGenerations = 1000;


    public static void main(String[] args) {

        /**
         *
         * As a reminder:
         *  0 = Empty
         *  1 = Wall
         *  2 = Starting position
         *  3 = Route
         *  4 = Goal position
         */
        Maze maze = new Maze(new int[][] {
            {0, 0, 0, 0, 1, 0, 1, 3, 2},
            {1, 0, 1, 1, 1, 0, 1, 3, 1},
            {1, 0, 0, 1, 3, 3, 3, 3, 1},
            {3, 3, 3, 1, 3, 1, 1, 0, 1},
            {3, 1, 3, 3, 3, 1, 1, 0, 0},
            {3, 3, 1, 1, 1, 1, 0, 1, 1},
            {1, 3, 0, 1, 3, 3, 3, 3, 3},
            {0, 3, 1, 1, 3, 1, 0, 1, 3},
            {1, 3, 3, 3, 3, 1, 1, 1, 4}
        });
    }
}
