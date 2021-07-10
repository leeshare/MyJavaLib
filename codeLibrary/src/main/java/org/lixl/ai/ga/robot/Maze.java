package org.lixl.ai.ga.robot;

import java.util.ArrayList;

/**
 * 这个类抽象一个迷宫,让一个机器人在航行其中.
 * 这个迷宫相当于一个整型的二维数组,使用不同的环境类型,使用以下整型:
 * 0 = Empty
 * 1 = Wall
 * 2 = Starting position
 * 3 = Route (路线)
 * 4 = Goal position
 * 这里最重要的方法是 scoreRoute, 它返回一个 fitness score for a path; 这就是 遗传算法将要优化的分值
 * Created by lxl on 19/11/29.
 */
public class Maze {
    private final int maze[][];
    private int startPosition[] = {-1, -1};

    public Maze(int maze[][]) {
        this.maze = maze;
    }

    /**
     * 获取迷宫起始位置
     *
     * @return int[] x,y start position of maze
     */
    public int[] getStartPosition() {
        // Check we already found start position
        if (this.startPosition[0] != -1 && this.startPosition[1] != -1) {
            return this.startPosition;
        }

        //Default return value
        int startPosition[] = {0, 0};

        // Loop over rows
        for (int rowIndex = 0; rowIndex < this.maze.length; rowIndex++) {
            // Loop over columns
            for (int colIndex = 0; colIndex < this.maze[rowIndex].length; colIndex++) {
                // 2 is the type for start position
                if (this.maze[rowIndex][colIndex] == 2) {
                    this.startPosition = new int[]{colIndex, rowIndex};
                    return new int[]{colIndex, rowIndex};
                }
            }

        }

        return startPosition;

    }

    /**
     * 获取迷宫某位置的值
     *
     * @param x
     * @param y
     * @return
     */
    public int getPositionValue(int x, int y) {
        if (x < 0 || y < 0 || x >= this.maze.length || y >= this.maze[0].length) {
            return 1;
        }
        return this.maze[y][x];
    }

    public boolean isWall(int x, int y) {
        return (this.getPositionValue(x, y) == 1);
    }

    public int getMaxX() {
        return this.maze[0].length - 1;
    }

    public int getMaxY() {
        return this.maze.length - 1;
    }

    /**
     * Scores a maze route
     * <p>
     * 这个方法检查一个用数组给出的路线,并且为每走正确一步加一点.
     * 我们同样要小心不能奖励重复访问正确的路线,否则我们会得到一个无限的分数,通过前后摆动的路线
     *
     * @param route
     * @return
     */
    public int scoreRoute(ArrayList<int[]> route) {
        int score = 0;
        boolean visited[][] = new boolean[this.getMaxY() + 1][this.getMaxX() + 1];

        // Loop over route and score each move
        for (Object routeStep : route) {
            int step[] = (int[]) routeStep;
            if (this.maze[step[1]][step[0]] == 3 && visited[step[1]][step[0]] == false) {
                // Increase score for correct move
                score++;
                // Remove reward
                visited[step[1]][step[0]] = true;
            }
        }

        return score;
    }
}
