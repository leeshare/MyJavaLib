package org.lixl.spring.aop;

/**
 * Created by Administrator on 7/6/2018.
 */
public class StockException extends Exception {
    private static final long serialVersionUID = 5377570098437361228L;

    public StockException() {
        super();
    }

    public StockException(String message) {
        super(message);
    }
}
