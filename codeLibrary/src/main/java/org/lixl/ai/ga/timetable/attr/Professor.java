package org.lixl.ai.ga.timetable.attr;

/**
 * 教授
 * Created by Administrator on 12/18/2019.
 */
public class Professor {
    private final int professorId;
    private final String professorName;

    public Professor(int professorId, String professorName) {
        this.professorId = professorId;
        this.professorName = professorName;
    }

    public int getProfessorId() {
        return this.professorId;
    }

    public String getProfessorName() {
        return this.professorName;
    }

}
