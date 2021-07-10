package org.lixl.multithreading.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TimerTest {
    private static Timer timer = new Timer();

    public static class MyTask extends TimerTask {
        int count = 0;
        int type = 0;

        public MyTask(int type) {
            this.type = type;
        }

        @Override
        public void run() {
            try {
                System.out.println("运行了！时间为：" + new Date());
                //Thread.sleep(4000);
                if (type == 2) {
                    //this.cancel();
                    //timer.cancel();
                }
                if (count == 5) {
                    timer.cancel();
                }
                count++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        try {
            MyTask task = new MyTask(1);
            MyTask task2 = new MyTask(2);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateString = "2021-07-02 20:37:10";
            Date dateRef = sdf.parse(dateString);
            System.out.println("字符串时间：" + dateRef.toLocaleString() + " 当前时间：" + new Date().toLocaleString());
            //timer.schedule(task, dateRef, 2000);
            //timer.schedule(task2, dateRef, 2000);

            timer.scheduleAtFixedRate(task2, dateRef, 3000);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
