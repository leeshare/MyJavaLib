package org.lixl.ai.ga.timetable;

import org.lixl.ai.ga.timetable.attr.Class;
/**
 * Created by Administrator on 12/19/2019.
 */
public class TimetableGA {
    private static int maxGeneration = 3000;

    public static void main(String[] args) {
        Timetable timetable = initializeTimetable();

        GeneticAlgorithm ga = new GeneticAlgorithm(100, 0.001, 0.9, 2, 5);

        Population population = ga.initPopulation(timetable);

        ga.evalPopulation(population, timetable);

        int generation = 1;

        while(ga.isTerminationConditionMet(generation, maxGeneration) == false
                && ga.isTerminationConditionMet(population) == false) {
            System.out.println("G" + generation + " 最强壮个体为：" + population.getFittest(0).getFitness());

            population = ga.crossoverPopulation(population);

            population = ga.mutatePopulation(population, timetable);

            ga.evalPopulation(population, timetable);

            generation++;
        }

        timetable.createClasses(population.getFittest(0));
        System.out.println();
        System.out.println("解决方案已找到，在第" + generation + "代。");
        System.out.println("最终解决方案的适应度是：" + population.getFittest(0).getFitness());
        //System.out.println("Clashes:" + timetable.calcClashes());
        int clashNum = timetable.calcClashes();
        System.out.println("Clashes:" + clashNum);
        if(clashNum > 0) {
            timetable.calcClashesAndPrint();
        }


        System.out.println();
        Class[] classes = timetable.getClasses();
        int classIndex = 1;
        for(Class bestClass : classes) {
            System.out.print("--------");
            System.out.print("Class " + (bestClass.getClassId() < 10 ? "0" + bestClass.getClassId() : "" + bestClass.getClassId()) + ": ");
            System.out.print(" Group: " + timetable.getGroup(bestClass.getGroupId()).getGroupId());
            System.out.print("Module: " + timetable.getModule(bestClass.getModuleId()).getModuleName());
            System.out.print(" Room: " + timetable.getRoom(bestClass.getRoomId()).getRoomNumber());
            System.out.print(" Professor: " + timetable.getProfessor(bestClass.getProfessorId()).getProfessorName());
            System.out.print(" Time: " + timetable.getTimeslot(bestClass.getTimeslotId()).getTimeslot());
            System.out.println("--------");
            classIndex++;
        }

    }

    private static Timetable initializeTimetable() {
        Timetable timetable = new Timetable();

        //教室
        timetable.addRoom(1, "A1", 15);
        timetable.addRoom(2, "B1", 30);
        timetable.addRoom(4, "D1", 20);
        timetable.addRoom(5, "F1", 25);

        //时间段
        timetable.addTimeslot(1, "Mon 9:00 - 11:00");
        timetable.addTimeslot(2, "Mon 11:00 - 13:00");
        timetable.addTimeslot(3, "Mon 13:00 - 15:00");

        timetable.addTimeslot(4, "Tue 9:00 - 11:00");
        timetable.addTimeslot(5, "Tue 11:00 - 13:00");
        timetable.addTimeslot(6, "Tue 13:00 - 15:00");

        timetable.addTimeslot(7, "Wed 9:00 - 11:00");
        timetable.addTimeslot(8, "Wed 11:00 - 13:00");
        timetable.addTimeslot(9, "Wed 13:00 - 15:00");

        timetable.addTimeslot(10, "Thu 9:00 - 11:00");
/*        timetable.addTimeslot(11, "Thu 11:00 - 13:00");
        timetable.addTimeslot(12, "Thu 13:00 - 15:00");

        timetable.addTimeslot(13, "Fri 9:00 - 11:00");
        timetable.addTimeslot(14, "Fri 11:00 - 13:00");
        timetable.addTimeslot(15, "Fri 13:00 - 15:00");
*/
        //教授
        timetable.addProfessor(1, "史密斯教授");
        timetable.addProfessor(2, "米切尔夫人");
        timetable.addProfessor(3, "威廉姆教授");
        timetable.addProfessor(4, "汤普森教授");

        //课程
        timetable.addModule(1, "cs1", "计科", new int[]{1, 2});
        timetable.addModule(2, "en1", "英语", new int[]{1, 3});
        timetable.addModule(3, "ma1", "数学", new int[]{1, 2});
        timetable.addModule(4, "ph1", "物理", new int[]{3, 4});
        timetable.addModule(5, "hi1", "历史", new int[]{4});
        timetable.addModule(6, "dr1", "戏剧", new int[]{1, 4});  //Drama 戏剧

        //学生组 <包含id  学生数量 和 他们所选的 课程(modules)>
        timetable.addGroup(1, 10, new int[]{1, 3, 4});
        timetable.addGroup(2, 30, new int[]{2, 3, 5, 6});
        timetable.addGroup(3, 18, new int[]{3, 4, 5});
        timetable.addGroup(4, 25, new int[]{1, 4});
        timetable.addGroup(5, 20, new int[]{2, 3, 5});
        timetable.addGroup(6, 22, new int[]{1, 4, 5});
        timetable.addGroup(7, 16, new int[]{1, 3});
        timetable.addGroup(8, 18, new int[]{2, 6});
        timetable.addGroup(9, 24, new int[]{1, 6});
        timetable.addGroup(10, 25, new int[]{3, 4});

        return timetable;
    }
}
