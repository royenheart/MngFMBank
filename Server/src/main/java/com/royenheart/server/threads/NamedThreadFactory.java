package com.royenheart.server.threads;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class NamedThreadFactory implements ThreadFactory {

    private static final AtomicInteger poolNumber = new AtomicInteger(1);
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final ThreadGroup threadsGrp;
    private String poolName;

    public NamedThreadFactory(String name) {
        this.poolName = name + "-" + poolNumber.getAndIncrement() + "-thread-";
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
