package test;

import java.lang.ref.WeakReference;

public class TestWeakReference {

    public static void main(String[] args) {
        //即使有car指向对象，但进行到while循环时，car已经没有被使用了，所以被优化了。
        Car car = new Car(2000, "silver");
        WeakReference<Car> weakCar = new WeakReference<>(car);
        int i = 0;
        while (true) {
            //这句是一个对car对象的强引用
            //System.out.println("这是一个强引用对'car'" + car);
            //如果有上面的强引用，那么WeakReference就不起作用了。
            if (weakCar.get() != null) {
                i++;
                //每次循环只有这个弱引用 weakCar 了。
                System.out.println("Object is alive for " + i + " loops - " + weakCar);
            } else {
                System.out.println("Object has bean collected");
                break;
            }
        }
    }
}
