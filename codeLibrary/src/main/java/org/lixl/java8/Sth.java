package org.lixl.java8;

/**
 * Created by Administrator on 4/4/2018.
 */
public class Sth {

    /*
    public static void main(String[] args){

        (new Sth()).doOptionals();
        (new Sth()).doStreamsFilterSortedMap();
    }

    public void doConsumers(){
        Consumer<Person> greeter = (p) -> System.out.println("Hello, " + p.firstName);
        greeter.accept(new Person("Luke", "Skywalker"));
    }

    public void doComparators(){
        Comparator<Person> comparator = (p1, p2) -> p1.firstName.compareTo(p2.firstName);
        Person p1 = new Person("John", "Doe");
        Person p2 = new Person("Alice", "Wonderland");

        comparator.compare(p1, p2);     // > 0
        comparator.reversed().compare(p1, p2);  // < 0
    }

    public void doOptionals(){
        Optional<String> optional = Optional.of("bam");
        optional.isPresent();       //true
        optional.get();             //"bam"
        optional.orElse("fallback");    //"bam"
        optional.ifPresent((s) -> System.out.println(s.charAt(0)));     //"b"
    }

    public void doStreamsFilterSortedMap(){
        List<String> stringCollection = new ArrayList<>();
        stringCollection.add("ddd2");
        stringCollection.add("aaa2");
        stringCollection.add("bbb1");
        stringCollection.add("aaa1");
        stringCollection.add("bbb3");
        stringCollection.add("ccc");
        stringCollection.add("bbb2");
        stringCollection.add("ddd1");

        stringCollection.stream().filter((s) -> s.startsWith("a")).forEach(System.out::println);

        stringCollection.stream().sorted().filter((s) -> s.startsWith("a")).forEach(System.out::println);
        //sorted 只是创建一个流对象排序的视图，并不改变原来集合中元素的顺序
        System.out.println(stringCollection);

        stringCollection.stream().map(String::toUpperCase).sorted((a, b) -> b.compareTo(a)).forEach(System.out::println);
    }*/

}

class Person {
    String firstName;
    String lastName;

    Person() {}

    Person(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
    }
}

interface PersonFactory<P extends Person> {
    P create(String firstName, String lastName);
}