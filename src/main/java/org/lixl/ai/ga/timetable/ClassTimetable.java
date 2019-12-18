package org.lixl.ai.ga.timetable;

import org.lixl.ai.ga.timetable.attr.*;
import org.lixl.ai.ga.timetable.attr.Class;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Administrator on 12/18/2019.
 */
public class ClassTimetable {
    private final HashMap<Integer, Room> rooms;
    private final HashMap<Integer, Professor> professors;
    private final HashMap<Integer, Module> modules;
    private final HashMap<Integer, Group> groups;
    private final HashMap<Integer, Timeslot> timeslots;

    private Class[] classes;
    private int numClasses = 0;

    public ClassTimetable() {
        this.rooms = new HashMap<>();
        this.professors = new HashMap<>();
        this.modules = new HashMap<>();
        this.groups = new HashMap<>();
        this.timeslots = new HashMap<>();
    }

    public ClassTimetable(ClassTimetable cloneable) {
        this.rooms = cloneable.getRooms();
    }

    public void addRoom(int roomId, String roomName, int capacity) {
        this.rooms.put(roomId, new Room(roomId, roomName, capacity));
    }

    public void addProfessor(int professorId, String professorName) {
        this.professors.put(professorId, new Professor(professorId, professorName));
    }

    public void addModule(int moduleId, String moduleCode, String module, int[] professorIds) {
        this.modules.put(moduleId, new Module(moduleId, moduleCode, module, professorIds));
    }

    public void addGroup(int groupId, int groupSize, int[] moduleIds) {
        this.groups.put(groupId, new Group(groupId, groupSize, moduleIds));
        this.numClasses = 0;
    }

    public void addTimeslot(int timeslotId, String timeslot) {
        this.timeslots.put(timeslotId, new Timeslot(timeslotId, timeslot));
    }

    public void createClasses(Individual individual) {
        //初始化 classes
        Class[] classes = new Class[this.getNumClasses()];
    }


    public Room getRoom(int roomId) {
        if(!this.rooms.containsKey(roomId)) {
            System.out.println("教室不存在此编号：" + roomId);
        }
        return this.rooms.get(roomId);
    }

    public HashMap<Integer, Room> getRooms() {
        return this.rooms;
    }

    /**
     * 获取需要排课的班级数
     * @return
     */
    public int getNumClasses() {
        if(this.numClasses > 0) {
            return this.numClasses;
        }

        // 下面这里没看明白 怎么回事
        int numClasses = 0;
        Group[] groups = (Group[]) this.groups.values().toArray(new Group[this.groups.size()]);
        for(Group group : groups) {
            numClasses += group.getModuleIds().length;
        }

        /*Iterator iterator = this.groups.entrySet().iterator();
        while(iterator.hasNext()) {
            Map.Entry<>
        }
        for(Group group : this.groups) {

        }*/
        this.numClasses = numClasses;

        return this.numClasses;
    }

}
