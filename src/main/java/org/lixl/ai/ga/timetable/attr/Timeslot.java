package org.lixl.ai.ga.timetable.attr;

/**
 * Created by Administrator on 12/18/2019.
 */
public class Timeslot {
    private final int timeslotId;
    private final String timeslot;

    public Timeslot(int timeslotId, String timeslot) {
        this.timeslot = timeslot;
        this.timeslotId = timeslotId;
    }

    public int getTimeslotId() {
        return this.timeslotId;
    }

    public String getTimeslot() {
        return this.timeslot;
    }
}
