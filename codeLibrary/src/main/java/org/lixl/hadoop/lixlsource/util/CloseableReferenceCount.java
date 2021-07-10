package org.lixl.hadoop.lixlsource.util;

import com.google.common.base.Preconditions;

import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.ClosedChannelException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 一个可关闭的对象——维持一个引用数
 * Created by Administrator on 1/15/2020.
 */
public class CloseableReferenceCount {
    /**
     * 二进制掩码代表一个关闭的域套接字
     */
    private static final int STATUS_CLOSED_MASK = 1 << 30;
    //1的二进制补码 0000 00001, 向左移动30位，即为 2^30  =  1,073,741,824
    //注：int的范围是 -2^31 ~ 2^31-1                        2,147,483,647

    /**
     * 二进制的状态
     * Bit 30: 0 = open, 1 = closed.
     * Bit 29 to 0: 引用数
     */
    private final AtomicInteger status = new AtomicInteger(0);

    public CloseableReferenceCount() {
    }

    /**
     * 增加引用数
     *
     * @throws ClosedChannelException
     */
    public void reference() throws ClosedChannelException {
        int curBits = status.incrementAndGet();
        // & 是两个数的二进制 再进行与操作， 比如 7 & 6 = 6; 7 | 6 = 7;
        if ((curBits & STATUS_CLOSED_MASK) != 0) {
            status.decrementAndGet();
            throw new ClosedChannelException();
        }
    }

    /**
     * 减少引用数
     *
     * @return
     */
    public boolean unreference() {
        int newVal = status.decrementAndGet();
        Preconditions.checkState(newVal != 0xffffffff, "调用unreference当引用数已经为0了。");
        return newVal == STATUS_CLOSED_MASK;
    }

    /**
     * 减少引用数，检查并确保“可关闭引用数”未关闭
     *
     * @throws ClosedChannelException
     */
    public void unreferenceCheckClosed() throws ClosedChannelException {
        int newVal = status.decrementAndGet();
        if ((newVal & STATUS_CLOSED_MASK) != 0) {
            throw new AsynchronousCloseException();
        }
    }

    /**
     * 如果此状态当前为true 则返回true
     *
     * @return
     */
    public boolean isOpen() {
        return ((status.get() & STATUS_CLOSED_MASK) == 0);
    }

    /**
     * 标识状态为关闭
     *
     * @return
     * @throws ClosedChannelException
     */
    public int setClosed() throws ClosedChannelException {
        while (true) {
            int curBits = status.get();
            if ((curBits & STATUS_CLOSED_MASK) != 0) {
                throw new ClosedChannelException();
            }
            if (status.compareAndSet(curBits, curBits | STATUS_CLOSED_MASK)) {
                return curBits & (~STATUS_CLOSED_MASK);
            }
        }
    }

    /**
     * 获取当前引用数
     *
     * @return
     */
    public int getReferenceCount() {
        return status.get() & (~STATUS_CLOSED_MASK);
    }

}
