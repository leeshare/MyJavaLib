package org.lixl.opensource.drools.pojo;

public class Person {
    private String name;    //姓名
    private String age;     //年龄
    private String className;   //所在班级
    private String personInfo;

    public Person(){}
    public Person(String name, String age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getPersonInfo() {
        return personInfo;
    }

    public void setPersonInfo(String sex) {
        this.personInfo = sex;
    }
}
