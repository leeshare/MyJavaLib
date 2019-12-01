package org.lixl.ai.ga.robot;

import java.util.ArrayList;

/**
 * 一个机器人的抽象.给它一个迷宫和一个训练集合,它将尝试通过并到达终点
 * Created by lxl on 19/12/1.
 */
public class Robot {
    private enum Direction {NORTH, EAST, SOUTH, WEST};

    private int xPosition;
    private int yPosition;
    private Direction heading;
    int maxMoves;
    int moves;
    private int sensorVal;
    private final int sensorActions[];
    private Maze maze;
    private ArrayList<int[]> route;

    public Robot(int[] sensorActions, Maze maze, int maxMoves) {
        this.sensorActions = this.calcSensorActions(sensorActions);
        this.maze = maze;
        int startPos[] = this.maze.getStartPosition();
        this.xPosition = startPos[0];
        this.yPosition = startPos[1];
        this.sensorVal = -1;
        this.heading = Direction.EAST;
        this.maxMoves = maxMoves;
        this.moves = 0;
        this.route = new ArrayList<int[]>();
        this.route.add(startPos);
    }

    public void run() {

    }

    private int[] calcSensorActions(int[] sensorActionsStr) {

    }
}
