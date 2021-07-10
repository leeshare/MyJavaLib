package org.lixl.ai.ga.robot;

import java.util.ArrayList;

/**
 * 一个机器人的抽象.给它一个迷宫和一个训练集合,它将尝试通过并到达终点
 * Created by lxl on 19/12/1.
 */
public class Robot {
    private enum Direction {NORTH, EAST, SOUTH, WEST}

    ;

    private int xPosition;
    private int yPosition;
    private Direction heading;
    int maxMoves;
    int moves;
    private int sensorVal;
    private final int sensorActions[];
    private Maze maze;
    private ArrayList<int[]> route;

    /**
     * 初始化一个机器人
     *
     * @param sensorActions 这个字符串用于 映射 传感器 到 动作
     * @param maze          迷宫
     * @param maxMoves      机器人在迷宫可以移动的最大步数
     */
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

    /**
     * 执行机器人动作基于传感器的输入
     */
    public void run() {
        while (true) {
            this.moves++;
            //Break if the robot stops moving
            if (this.getNextAction() == 0) {
                return;
            }
            //跳出：如果我们达到目标
            if (this.maze.getPositionValue(this.xPosition, this.yPosition) == 4) {
                return;
            }
            //跳出：如果我们达到最大移动步数
            if (this.moves > this.maxMoves) {
                return;
            }
            //执行动作
            this.makeNextAction();
        }

    }

    /**
     * 映射 机器人传感器数据的二进制字符串 到 动作
     *
     * @param sensorActionsStr
     * @return
     */
    private int[] calcSensorActions(int[] sensorActionsStr) {
        //这里有多少动作？
        int numActions = (int) sensorActionsStr.length / 2;
        int sensorActions[] = new int[numActions];

        for (int sensorValue = 0; sensorValue < numActions; sensorValue++) {
            //Get sensor action
            int sensorAction = 0;
            if (sensorActionsStr[sensorValue * 2] == 1) {
                sensorAction += 2;
            }
            if (sensorActionsStr[(sensorValue * 2) + 1] == 1) {
                sensorAction += 1;
            }

            sensorActions[sensorValue] = sensorAction;
        }
        return sensorActions;
    }

    /**
     * 执行下一个动作
     */
    public void makeNextAction() {
        //If move forward
        if (this.getNextAction() == 1) {
            int currentX = this.xPosition;
            int currentY = this.yPosition;

            if (Direction.NORTH == this.heading) {
                this.yPosition += -1;
                if (this.yPosition < 0) {
                    this.yPosition = 0;
                }
            } else if (Direction.EAST == this.heading) {
                this.xPosition += 1;
                if (this.xPosition > this.maze.getMaxX()) {
                    this.xPosition = this.maze.getMaxX();
                }
            } else if (Direction.WEST == this.heading) {
                this.xPosition -= 1;
                if (this.xPosition < 0) {
                    this.xPosition = 0;
                }
            } else if (Direction.SOUTH == this.heading) {
                this.yPosition += 1;
                if (this.yPosition > this.maze.getMaxY()) {
                    this.yPosition = this.maze.getMaxY();
                }
            }

            //We can't move here
            if (this.maze.isWall(this.xPosition, this.yPosition) == true) {
                this.xPosition = currentX;
                this.yPosition = currentY;
            } else {
                if (currentX != this.xPosition || currentY != this.yPosition) {
                    //向前走一步
                    this.route.add(this.getPosition());
                }
            }
        }
        // Move clockwise  按顺时针移动
        else if (this.getNextAction() == 2) {
            if (Direction.NORTH == this.heading) {
                this.heading = Direction.EAST;
            } else if (Direction.EAST == this.heading) {
                this.heading = Direction.SOUTH;
            } else if (Direction.SOUTH == this.heading) {
                this.heading = Direction.WEST;
            } else if (Direction.WEST == this.heading) {
                this.heading = Direction.NORTH;
            }
        }
        // Move anti-clockwise  按逆时针移动
        else if (this.getNextAction() == 3) {
            if (Direction.NORTH == this.heading) {
                this.heading = Direction.WEST;
            } else if (Direction.EAST == this.heading) {
                this.heading = Direction.NORTH;
            } else if (Direction.SOUTH == this.heading) {
                this.heading = Direction.EAST;
            } else if (Direction.WEST == this.heading) {
                this.heading = Direction.SOUTH;
            }
        }

        //Reset sensor value
        this.sensorVal = -1;
    }

    /**
     * 获取下一个动作依赖的传感器映射
     *
     * @return
     */
    public int getNextAction() {
        return this.sensorActions[this.getSensorValue()];
    }

    /**
     * 获取传感器的值
     *
     * @return
     */
    public int getSensorValue() {
        //如果感知器的值已被计算过了
        if (this.sensorVal > -1) {
            return this.sensorVal;
        }

        boolean frontSensor, frontLeftSensor, frontRightSensor, leftSensor, rightSensor, backSensor;
        frontSensor = frontLeftSensor = frontRightSensor = leftSensor = rightSensor = backSensor = false;

        //找到哪个感知器已激活了
        if (this.getHeading() == Direction.NORTH) {
            frontSensor = this.maze.isWall(this.xPosition, this.yPosition - 1);
            frontLeftSensor = this.maze.isWall(this.xPosition - 1, this.yPosition - 1);
            frontRightSensor = this.maze.isWall(this.xPosition + 1, this.yPosition - 1);
            leftSensor = this.maze.isWall(this.xPosition - 1, this.yPosition);
            rightSensor = this.maze.isWall(this.xPosition + 1, this.yPosition);
            backSensor = this.maze.isWall(this.xPosition, this.yPosition + 1);
        } else if (this.getHeading() == Direction.EAST) {
            frontSensor = this.maze.isWall(this.xPosition + 1, this.yPosition);
            frontLeftSensor = this.maze.isWall(this.xPosition + 1, this.yPosition - 1);
            frontRightSensor = this.maze.isWall(this.xPosition + 1, this.yPosition + 1);
            leftSensor = this.maze.isWall(this.xPosition, this.yPosition - 1);
            rightSensor = this.maze.isWall(this.xPosition, this.yPosition + 1);
            backSensor = this.maze.isWall(this.xPosition - 1, this.yPosition);
        } else if (this.getHeading() == Direction.SOUTH) {
            frontSensor = this.maze.isWall(this.xPosition, this.yPosition + 1);
            frontLeftSensor = this.maze.isWall(this.xPosition + 1, this.yPosition + 1);
            frontRightSensor = this.maze.isWall(this.xPosition - 1, this.yPosition + 1);
            leftSensor = this.maze.isWall(this.xPosition + 1, this.yPosition);
            rightSensor = this.maze.isWall(this.xPosition - 1, this.yPosition);
            backSensor = this.maze.isWall(this.xPosition, this.yPosition - 1);
        } else if (this.getHeading() == Direction.WEST) {
            frontSensor = this.maze.isWall(this.xPosition - 1, this.yPosition);
            frontLeftSensor = this.maze.isWall(this.xPosition - 1, this.yPosition + 1);
            frontRightSensor = this.maze.isWall(this.xPosition - 1, this.yPosition - 1);
            leftSensor = this.maze.isWall(this.xPosition, this.yPosition + 1);
            rightSensor = this.maze.isWall(this.xPosition, this.yPosition - 1);
            backSensor = this.maze.isWall(this.xPosition + 1, this.yPosition);
        }

        //这里不太懂？ 返回的是 二进制的十进制值吗
        //Calculate sensor value
        int sensorVal = 0;
        if (frontSensor == true) {
            sensorVal += 1;
        }
        if (frontLeftSensor == true) {
            sensorVal += 2;
        }
        if (frontRightSensor == true) {
            sensorVal += 4;
        }
        if (leftSensor == true) {
            sensorVal += 8;
        }
        if (rightSensor == true) {
            sensorVal += 16;
        }
        if (backSensor == true) {
            sensorVal += 32;
        }

        this.sensorVal = sensorVal;
        return sensorVal;
    }

    /**
     * 获取机器人的位置
     *
     * @return
     */
    public int[] getPosition() {
        return new int[]{this.xPosition, this.yPosition};
    }

    /**
     * 获取机器人的朝向
     *
     * @return
     */
    private Direction getHeading() {
        return this.heading;
    }

    /**
     * 返回机器人在迷宫中已走的路线
     *
     * @return
     */
    public ArrayList<int[]> getRoute() {
        return this.route;
    }

    /**
     * 返回路线的可打印出来的形式
     *
     * @return
     */
    public String printRoute() {
        String route = "";
        for (Object routeStep : this.route) {
            int step[] = (int[]) routeStep;
            route += "{" + step[0] + "," + step[1] + "}";
        }
        return route;
    }

}
