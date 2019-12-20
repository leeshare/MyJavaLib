package org.lixl.ai.ga.timetable;

import org.lixl.ai.ga.timetable.attr.*;
import org.lixl.ai.ga.timetable.attr.Class;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Timetable时间表 是这个GA的主要评估类
 * 一个时间表表现一个人类可以理解的形式的解决方案，而不是一个体或一染色体
 * 这个时间表类，然后可以读取一个染色体后生成一个时间表，并且最终能评估这个时间表通过适应度和冲突次数
 *
 * 这里最重要的方法是 createClasses 和 calcClashes
 *
 * createClasses 方法接受一个个体（Individual 实际上是一个 chromosome），
 *      解包其染色体，从其遗传信息中创建Class对象
 *      Class对象是轻量级的：它们仅包含getters和setters，但它们比直接操作染色体更方便
 *
 * calcClashes 方法用于 GeneticAlgorithm.calcFitness （计算每个传入个体的适应度）
 *      它要求先运行 createClasses
 *      calcClashes 着眼于(look at) 被createClasses创建的Class对象，
 *      并且得出(figure out)有多少硬约束(hard constraints)是违反的(violate 违反)
 *
 * Created by Administrator on 12/18/2019.
 */
public class Timetable {
    private final HashMap<Integer, Room> rooms;     //教室
    private final HashMap<Integer, Professor> professors;   //教授
    private final HashMap<Integer, Module> modules;     //课程
    private final HashMap<Integer, Group> groups;       //学生组
    private final HashMap<Integer, Timeslot> timeslots; //时间段

    private Class[] classes;    //班数组
    private int numClasses = 0;

    /**
     * 初始化课程表
     */
    public Timetable() {
        this.rooms = new HashMap<>();
        this.professors = new HashMap<>();
        this.modules = new HashMap<>();
        this.groups = new HashMap<>();
        this.timeslots = new HashMap<>();
    }

    /**
     * 克隆一个课程表。
     * 在评估一个课程表前，我们克隆一个独一无二的容器用于每个类的创建 createClasses
     * 诚然，这完全没有必要（其实没有什么大不了 推翻再利用）
     * 但以后优化时，我们将讨论用多线程来计算适应度，并且为了做这些，我们需要分离对象，这样每个线程不依赖其他线程
     * 所以在优化前，这个方法是没必要的，但在以后优化时，我们将看到其作用所在。
     * @param cloneable
     */
    public Timetable(Timetable cloneable) {
        this.rooms = cloneable.getRooms();
        this.professors = cloneable.getProfessors();
        this.modules = cloneable.getModules();
        this.groups = cloneable.getGroups();
        this.timeslots = cloneable.getTimeslots();
    }

    private HashMap<Integer, Group> getGroups() {
        return this.groups;
    }
    private HashMap<Integer, Timeslot> getTimeslots() {
        return this.timeslots;
    }
    private HashMap<Integer, Module> getModules() {
        return this.modules;
    }
    private HashMap<Integer, Professor> getProfessors() {
        return this.professors;
    }

    /**
     * 加一个新教室
     * @param roomId
     * @param roomName
     * @param capacity
     */
    public void addRoom(int roomId, String roomName, int capacity) {
        this.rooms.put(roomId, new Room(roomId, roomName, capacity));
    }

    /**
     * 加一个新教授
     * @param professorId
     * @param professorName
     */
    public void addProfessor(int professorId, String professorName) {
        this.professors.put(professorId, new Professor(professorId, professorName));
    }

    /**
     * 加一个新课程
     * @param moduleId
     * @param moduleCode
     * @param module
     * @param professorIds
     */
    public void addModule(int moduleId, String moduleCode, String module, int[] professorIds) {
        this.modules.put(moduleId, new Module(moduleId, moduleCode, module, professorIds));
    }

    /**
     * 加一个新学生组
     * @param groupId
     * @param groupSize
     * @param moduleIds
     */
    public void addGroup(int groupId, int groupSize, int[] moduleIds) {
        this.groups.put(groupId, new Group(groupId, groupSize, moduleIds));
        this.numClasses = 0;
    }

    /**
     * 加一个时间段
     * @param timeslotId
     * @param timeslot
     */
    public void addTimeslot(int timeslotId, String timeslot) {
        this.timeslots.put(timeslotId, new Timeslot(timeslotId, timeslot));
    }

    /**
     * 使用种群个体的染色体 创建班数组
     *
     * 给定一个染色体，将其解包，加入到一个班数组对象中
     * 这些班级对象将被 calcClashes 方法评估
     *      clacClashes 将循环这个班级并计算其在 时间段、教室、教授等的冲突数量
     * @param individual
     */
    public void createClasses(Individual individual) {
        //初始化 classes
        Class[] classes = new Class[this.getNumClasses()];

        int chromosome[] = individual.getChromosome();
        int chromosomePos = 0;
        int classIndex = 0;

        for(Group group : this.getGroupsAsArray()) {
            int moduleIds[] = group.getModuleIds();
            for(int moduleId : moduleIds) {
                classes[classIndex] = new Class(classIndex, group.getGroupId(), moduleId);

                classes[classIndex].addTimeslot(chromosome[chromosomePos]);
                chromosomePos++;

                classes[classIndex].setRoomId(chromosome[chromosomePos]);
                chromosomePos++;

                classes[classIndex].addProfessor(chromosome[chromosomePos]);
                chromosomePos++;

                classIndex++;
            }
        }

        this.classes = classes;
    }


    public Room getRoom(int roomId) {
        if(!this.rooms.containsKey(roomId)) {
            System.out.println("教室不存在此编号：" + roomId);
        }
        return (Room)this.rooms.get(roomId);
    }

    public HashMap<Integer, Room> getRooms() {
        return this.rooms;
    }

    public Room getRandomRoom() {
        Object[] roomsArray = this.rooms.values().toArray();
        Room room = (Room) roomsArray[(int)(roomsArray.length * Math.random())];
        return room;
    }

    public Professor getProfessor(int professorId) {
        return (Professor) this.professors.get(professorId);
    }

    public Module getModule(int moduleId) {
        return (Module) this.modules.get(moduleId);
    }

    public int[] getGroupModules(int groupId) {
        Group group = (Group) this.groups.get(groupId);
        return group.getModuleIds();
    }

    public Group getGroup(int groupId) {
        return (Group) this.groups.get(groupId);
    }

    /**
     * 获取所有 学生组
     * @return
     */
    public Group[] getGroupsAsArray() {

        return (Group[]) this.groups.values().toArray(new Group[this.groups.size()]);
    }

    public Timeslot getTimeslot(int timeslotId) {
        return (Timeslot) this.timeslots.get(timeslotId);
    }

    public Timeslot getRandomTimeslot() {
        Object[] timeslotArray = this.timeslots.values().toArray();
        Timeslot timeslot = (Timeslot)timeslotArray[(int)(timeslotArray.length * Math.random())];
        return timeslot;
    }

    public Class[] getClasses() {
        return this.classes;
    }

    /**
     * 获取需要排课的班级数
     * 班数 = 有多少个学生组， 每个学生组选了几门课
     * @return
     */
    public int getNumClasses() {
        if(this.numClasses > 0) {
            return this.numClasses;
        }

        // 下面这里没看明白 怎么回事
        //在看放入的是什么： this.groups.put(groupId, new Group(groupId, groupSize, modules));
        // 可知 groups的 key 是 groupId, value 是 Group对象
        int numClasses = 0;
        //下面是 取到所有 Group对象，并将其转化成定长数组
        Group[] groups = (Group[]) this.groups.values().toArray(new Group[this.groups.size()]);
        for(Group group : groups) {
            numClasses += group.getModuleIds().length;
        }

        /*Iterator iterator = this.groups.entrySet().iterator();
        while(iterator.hasNext()) {
            Map.Entry<Integer, Group> entity = iterator.next();
        }*/

        this.numClasses = numClasses;
        return this.numClasses;
    }

    /**
     * 计算 按染色体生产的班 的冲突数
     *
     * 这个类中最重要的方法：考虑一个候选排课表
     * 并且得出(figure out)有多少硬约束(hard constraints)是违反的(violate 违反)
     *
     * 执行这个方法需要 createClasses 首先执行
     *
     * 返回一个简单的数值，包含约束冲突（教授、时间段、或教室），且这个返回值用于 GeneticAlgorithm.calcFitness 方法
     *
     *
     * @return
     */
    public int calcClashes() {
        int clashes = 0;

        for(Class classA : this.classes) {
            int roomCapacity = this.getRoom(classA.getRoomId()).getRoomCapacity();
            int groupSize = this.getGroup(classA.getGroupId()).getGroupSize();

            //教室容量 < 学生组大小 就是冲突
            if(roomCapacity < groupSize) {
                clashes++;
            }

            //同教室、同时间段，但不同班 就是冲突
            for(Class classB : this.classes) {
                if(classA.getRoomId() == classB.getRoomId()
                        && classA.getTimeslotId() == classB.getTimeslotId()
                        && classA.getClassId() != classB.getClassId()
                        ) {
                    clashes++;
                    break;
                }
            }

            //同教授、同时间段，但不同班 就是冲突
            for(Class classB : this.classes) {
                if(classA.getProfessorId() == classB.getProfessorId()
                        && classA.getTimeslotId() == classB.getTimeslotId()
                        && classA.getClassId() != classB.getClassId()
                        ) {
                    clashes++;
                    break;
                }
            }
        }

        return clashes;
    }

    public int calcClashesAndPrint() {
        int clashes = 0;

        for(Class classA : this.classes) {
            int roomCapacity = this.getRoom(classA.getRoomId()).getRoomCapacity();
            int groupSize = this.getGroup(classA.getGroupId()).getGroupSize();

            //教室容量 < 学生组大小 就是冲突
            if(roomCapacity < groupSize) {
                clashes++;
                System.out.println("classId=" + classA.getClassId() + this.getRoom(classA.getRoomId()).getRoomNumber() +  "教室太小 教室=" + roomCapacity + " 学生数=" + groupSize);
            }

            //同教室、同时间段，但不同班 就是冲突
            for(Class classB : this.classes) {
                if(classA.getRoomId() == classB.getRoomId()
                        && classA.getTimeslotId() == classB.getTimeslotId()
                        && classA.getClassId() != classB.getClassId()
                        ) {
                    clashes++;
                    System.out.println("classId=" + classA.getClassId() + " 与" + classB.getClassId() + " 教室冲突");
                    break;
                }
            }

            //同教授、同时间段，但不同班 就是冲突
            for(Class classB : this.classes) {
                if(classA.getProfessorId() == classB.getProfessorId()
                        && classA.getTimeslotId() == classB.getTimeslotId()
                        && classA.getClassId() != classB.getClassId()
                        ) {
                    clashes++;
                    System.out.println("classId=" + classA.getClassId() + " 与" + classB.getClassId() + " 老师冲突");
                    break;
                }
            }
        }

        return clashes;
    }


}
