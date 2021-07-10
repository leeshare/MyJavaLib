package org.lixl.aop;

/**
 * Created by lxl on 18/9/28.
 */
public class LogHandler {
    public void LogBefore() {
        System.out.println("Log before method");
    }

    public void LogAfter() {
        System.out.println("Log after method");
    }
}
