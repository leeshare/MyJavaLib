public class Student {
    private String name;
    private Integer age;

    private static String school = "新东方";

    public Student(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public void printInfo(){
        System.out.println(this.name + " " + this.age + " " + Student.school);
    }

    public static void main(String[] args) {
        Student s = new Student("alice", 20);
        Student s2 = new Student("bob", 23);

        s.printInfo();
        s2.printInfo();
    }
}
