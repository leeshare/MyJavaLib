package test;

import java.io.*;

public class TestSerial implements Serializable {
    public byte version = 100;
    public byte count = 0;
}


class TestSerialMain {
    private static void createFile() throws IOException {
        //生成的文件 temp.out 存在于项目的根目录下
        FileOutputStream fos = new FileOutputStream("temp.out");
        ObjectOutputStream oos = new ObjectOutputStream(fos);

        TestSerial ts = new TestSerial();
        oos.writeObject(ts);

        oos.flush();
        oos.close();
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException{
        createFile();
        readFile();
    }

    public static void readFile() throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream("temp.out");
        ObjectInputStream ois = new ObjectInputStream(fis);

        TestSerial ts = (TestSerial) ois.readObject();

        System.out.println("version=" + ts.version);
    }
}
