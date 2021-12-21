package com.royenheart.server.threads;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程池命名线程工厂（用于区分业务）
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

    /**
     * 当新任务被提交时，根据原子线程数量分配线程ID
     * @param r 新的任务（线程）
     * @return 工厂方法，将线程加工后（分配ID）返回给线程池
     */
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
