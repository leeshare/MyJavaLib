package org.lixl.ai.ga.timetable.attr;

/**
 * Created by Administrator on 12/18/2019.
 */
public class Class {
    private final int classId;
    private final int groupId;
    private final int moduleId;
    private int professorId;
    private int timeslotId;     //时间段
    private int roomId;

    public Class(int classId, int groupId, int moduleId) {
        this.classId = classId;
        this.moduleId = moduleId;
        this.groupId = groupId;
    }

    public Class(Builder builder) {
        this.classId = builder.classId;
        this.groupId = builder.groupId;
        this.moduleId = builder.moduleId;
        this.professorId = builder.professorId;
        this.timeslotId = builder.timeslotId;
        this.roomId = builder.roomId;
    }

    public static class Builder {
        private final int classId;
        private final int groupId;
        private final int moduleId;
        private int professorId;
        private int timeslotId;     //时间段
        private int roomId;

        public Builder(int classId, int groupId, int moduleId) {
            this.classId = classId;
            this.groupId = groupId;
            this.moduleId = moduleId;
        }
        public Builder addProfessor(int professorId) {
            this.professorId = professorId;
            return this;
        }
        public Builder addTimeslot(int timeslotId) {
            this.timeslotId = timeslotId;
            return this;
        }
        public Builder addRoom(int roomId) {
            this.roomId = roomId;
            return this;
        }

        public Class builder() {
            return new Class(this);
        }
    }

    public void addProfessor(int professorId) {
        this.professorId = professorId;
    }
    public void addTimeslot(int timeslotId) {
        this.timeslotId = timeslotId;
    }
    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getClassId() {
        return this.classId;
    }
    public int getGroupId() {
        return this.groupId;
    }
    public int getModuleId(){
        return this.moduleId;
    }
    public int getProfessorId(){
        return this.professorId;
    }
    public int getTimeslotId(){
        return this.timeslotId;
    }
    public int getRoomId() {
        return this.roomId;
    }

    public String toString(){
        return " " + this.classId + " " + this.groupId + " " + this.moduleId + " " + this.professorId + " " + this.timeslotId + " " + this.roomId;
    }

    public static void main(String[] args) {
        Class aaa = new Builder(11, 22, 33).addProfessor(44).addTimeslot(55).addRoom(66).builder();
        System.out.println(aaa.toString());
    }

}
