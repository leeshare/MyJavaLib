package org.lixl.ai.ga.timetable.attr;

/**
 * Created by Administrator on 12/18/2019.
 */
public class Group {
    private final int groupId;
    private final int groupSize;
    private final int moduleIds[];

    public Group(int groupId, int groupSize, int moduleIds[]) {
        this.groupId = groupId;
        this.groupSize = groupSize;
        this.moduleIds = moduleIds;
    }

    public int getGroupId() {
        return this.groupId;
    }
    public int getGroupSize(){
        return this.groupSize;
    }
    public int[] getModuleIds(){
        return this.moduleIds;
    }
}
