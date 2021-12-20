package com.royenheart.server.threads;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程池命名线程工厂(更易于区分)
 * @author RoyenHeart
 */
public class NamedThreadFactory implements ThreadFactory {

    private static final AtomicInteger POOL_NUMBER = new AtomicInteger(1);
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final ThreadGroup threadsGrp;
    private final String poolName;

    public NamedThreadFactory(String name) {
        this.poolName = name + "-" + POOL_NUMBER.getAndIncrement() + "-thread-";
        SecurityManager s = System.getSecurityManager();
        threadsGrp = (s != null)?s.getThreadGroup():Thread.currentThread().getThreadGroup();
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread namedT = new Thread(threadsGrp, r, this.poolName + threadNumber.getAndIncrement(), 0);
        if (namedT.isDaemon()) {
            namedT.setDaemon(false);
        }
        if (namedT.getPriority() != Thread.NORM_PRIORITY) {
            namedT.setPriority(Thread.NORM_PRIORITY);
        }
        return namedT;
    }
}
