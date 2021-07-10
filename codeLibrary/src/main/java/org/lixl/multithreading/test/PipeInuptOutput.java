package org.lixl.multithreading.test;

import java.io.*;

public class PipeInuptOutput {

    public static class WriteData {
        public void writeMethod(PipedOutputStream out) {
            try {
                System.out.println("write :");
                for (int i = 0; i < 300; i++) {
                    String outData = "" + (i + 1);
                    out.write(outData.getBytes());
                    System.out.print(outData);
                }
                System.out.println();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void writeMethod2(PipedWriter out) {
            try {
                System.out.println("write :");
                for (int i = 0; i < 300; i++) {
                    String outData = "" + (i + 1);
                    out.write(outData);
                    System.out.print(outData);
                }
                System.out.println();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static class ReadData {
        public void readMethod(PipedInputStream input) {
            try {
                System.out.println("read :");
                byte[] byteArray = new byte[20];
                int readLength = input.read(byteArray);
                while (readLength != -1) {
                    String newData = new String(byteArray, 0, readLength);
                    System.out.print(newData);
                    readLength = input.read(byteArray);
                }
                System.out.println();
                input.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void readMethod2(PipedReader input) {
            try {
                System.out.println("read :");
                char[] byteArray = new char[20];
                int readLength = input.read(byteArray);
                while (readLength != -1) {
                    String newData = new String(byteArray, 0, readLength);
                    System.out.print(newData);
                    readLength = input.read(byteArray);
                }
                System.out.println();
                input.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        WriteData writeData = new WriteData();
        ReadData readData = new ReadData();

        PipedInputStream inputStream = new PipedInputStream();
        PipedOutputStream outputStream = new PipedOutputStream();

        PipedReader reader = new PipedReader();
        PipedWriter writer = new PipedWriter();

        //将两个Stream之间产生通信链接，这样才可以将数据进行输出与输入
        //outputStream.connect(inputStream);
        inputStream.connect(outputStream);

        reader.connect(writer);

        Thread t_r = new Thread() {
            @Override
            public void run() {
                readData.readMethod(inputStream);
                readData.readMethod2(reader);
            }
        };
        t_r.start();
        Thread.sleep(2000);
        Thread t_w = new Thread() {
            @Override
            public void run() {
                writeData.writeMethod(outputStream);
                writeData.writeMethod2(writer);
            }
        };
        t_w.start();

        //Thread.sleep(2000);
        //t_r.start();
    }
}
