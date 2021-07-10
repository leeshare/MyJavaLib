package org.lixl.ai.ga.timetable.attr;

/**
 * 课程组，包含课程信息 和 教授数组
 * Created by Administrator on 12/18/2019.
 */
public class Module {
    private final int moduleId;
    private final String moduleCode;
    private final String module;
    private final int professorIds[];

    public Module(int moduleId, String moduleCode, String module, int[] professorIds) {
        this.module = module;
        this.moduleId = moduleId;
        this.moduleCode = moduleCode;
        this.professorIds = professorIds;
    }

    public int getModuleId() {
        return this.moduleId;
    }

    public String getModuleCode() {
        return this.moduleCode;
    }

    public String getModuleName() {
        return this.module;
    }

    /**
     * 随机取一个“教授”Id
     *
     * @return
     */
    public int getRandomProfessorId() {
        int professorId = professorIds[(int) (professorIds.length * Math.random())];
        return professorId;
    }

}
