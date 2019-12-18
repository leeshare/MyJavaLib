package org.lixl.ai.ga.timetable.attr;

/**
 * Created by Administrator on 12/18/2019.
 */
public class Room {
    private final int roomId;
    private final String roomNumber;
    private final int capacity;     // 能力 容量

    public Room(int roomId, String roomNumber, int capacity) {
        this.roomId = roomId;
        this.roomNumber = roomNumber;
        this.capacity = capacity;
    }

    public int getRoomId() {
        return roomId;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public int getCapacity() {
        return this.capacity;
    }

}
