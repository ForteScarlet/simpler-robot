/*
 *
 *  * Copyright (c) 2020. ForteScarlet All rights reserved.
 *  * Project  simple-robot
 *  * File     MiraiAvatar.kt
 *  *
 *  * You can contact the author through the following channels:
 *  * github https://github.com/ForteScarlet
 *  * gitee  https://gitee.com/ForteScarlet
 *  * email  ForteScarlet@163.com
 *  * QQ     1149159218
 *
 */

package love.forte.simbot.component.lovelycat.utils;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;

/**
 * 懒清理的时间缓存类。
 * 通过可重入公平读写锁保证线程安全。
 *
 * @author ForteScarlet
 */
public class LazyTimeLimitCache<T> {
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final long time;
    private volatile long nextTime = -1;
    private volatile T entity;

    public LazyTimeLimitCache(long time, TimeUnit timeUnit) {
        this.time = timeUnit.toMillis(time);
    }

    public LazyTimeLimitCache(long time) {
        this.time = time;
    }

    /**
     * 刷新下次到期时间。
     */
    private void refreshTime() {
        nextTime = System.currentTimeMillis() + time;
    }

    /**
     * 判断是否已过到期时间。
     */
    private boolean isExpired() {
        return System.currentTimeMillis() > nextTime;
    }


    public void clean() {
        Lock writeLock = this.lock.writeLock();
        writeLock.lock();
        try {
            entity = null;
            nextTime = -1;
        } finally {
            writeLock.unlock();
        }
    }


    /**
     * 获取或计算。
     */
    public T compute(Supplier<T> computer) {
        Lock writeLock = this.lock.writeLock();
        // check time
        if (isExpired()) {
            writeLock.lock();
            try {
                if (isExpired()) {
                    refreshTime();
                    // compute.
                    entity = computer.get();
                }
            } finally {
                writeLock.unlock();
            }
        }

        T e;
        Lock readLock = this.lock.readLock();
        readLock.lock();
        try {
            e = entity;
        } finally {
            readLock.unlock();
        }

        if (e == null) {
            T en = entity;
            if (en != null) {
                e = en;
            } else {
                writeLock.lock();
                try {
                    en = entity;
                    if (en != null) {
                        e = en;
                    } else {
                        e = (entity = computer.get());
                    }
                } finally {
                    writeLock.unlock();
                }
            }
        }

        return e;
    }


}
