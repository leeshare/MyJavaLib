package org.lixl.pattern;

import java.util.Vector;

/**
 * 享元模式
 * 就是共享池
 */
public class L_Flyweight {
    public static class ConnectionPool {
        private Vector<Connection> pool;

        private String url = "jdbc:mysql://localhost:3306/test";
        private String username = "root";
        private String password = "root";
        private String driverClassName = "com.mysql.jdbc.Driver";

        private int poolSize = 100;

    }

    public static class Connection {

    }
}
