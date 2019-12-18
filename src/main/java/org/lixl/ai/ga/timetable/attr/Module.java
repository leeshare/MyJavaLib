package org.lixl.ai.ga.timetable.attr;

/**
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
        return moduleId;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public String getModule() {
        return module;
    }

    /**
     * 随机取一个“教授”Id
     * @return
     */
    public int getRandomProfessorId() {
        int professorId = professorIds[(int) (professorIds.length * Math.random())];
        return professorId;
    }

}
