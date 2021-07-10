package org.lixl.pattern;

import java.io.*;

/**
 * 原型模式
 */
public class E_Prototype implements Cloneable {

    public Object clone() throws CloneNotSupportedException {
        E_Prototype proto = (E_Prototype) super.clone();
        return proto;
    }

    public Object deepClone() throws IOException, ClassNotFoundException {
        //写入当前对象的二进制流
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(this);

        //读出二进制流产生的新对象
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bis);
        return ois.readObject();


    }
}
