package org.lixl.java.lang;

public enum Color {
    RED("红色", 1), GREEN("绿色", 2), BLANK("白色", 3), YELLOW("黄色", 4);
    //成员变量
    private String name;
    private int index;
    //构造方法
    private Color(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public int getIndex() {
        return this.index;
    }
    public void setIndex(int index) {
        this.index = index;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public static String getName(int index) {
        for(Color c : Color.values()) {
            if(c.getIndex() == index) {
                return c.getName();
            }
        }
        return null;
    }

}
