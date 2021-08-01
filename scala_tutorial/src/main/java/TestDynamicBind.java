public class TestDynamicBind {
    public static void main(String[] args) {
        Worker worker = new Worker();
        System.out.println(worker.name);
        worker.hello();

        System.out.println("==============");

        Person person = new Worker();       //子类 赋值给 父类
        System.out.println(person.name);    //java中 属性是静态绑定，只有方法才是动态绑定
        person.hello();     //这就是动态绑定

    }

    public static class Person {
        String name = "person";
        public void hello() {
            System.out.println("hello person");
        }
    }

    public static class Worker extends Person {
        String name = "worker";
        public void hello() {
            System.out.println("hello worker");
        }
    }

}
