package com.walm.learn.algorithm.ratelimit;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>SlidingWindowRateLimit</p>
 * <p>滑动窗口限流</p>
 *
 * @author wangjn
 * @since 2019-09-30
 */
@Slf4j
public class SlidingWindowRateLimit implements RateLimit, Runnable {

    /**
     * 阈值
     */
    private Integer limitCount;

    /**
     * 当前通过的请求数
     */
    private AtomicInteger passCount;

    /**
     * 窗口数
     */
    private Integer windowSize;

    /**
     * 每个窗口时间间隔大小
     */
    private long windowPeriod;
    private TimeUnit timeUnit;


    private Window[] windows;
    private volatile Integer windowIndex = 0;

    private Lock lock = new ReentrantLock();
    public SlidingWindowRateLimit(Integer limitCount) {
        // 默认统计qps, 窗口大小5
        this(limitCount, 5, 200, TimeUnit.MILLISECONDS);
    }

    /**
     * 统计总时间 = windowSize * windowPeriod
     */
    public SlidingWindowRateLimit(Integer limitCount, Integer windowSize, Integer windowPeriod, TimeUnit timeUnit) {
        this.limitCount = limitCount;
        this.windowSize = windowSize;
        this.windowPeriod = windowPeriod;
        this.timeUnit = timeUnit;
        this.passCount = new AtomicInteger(0);
        this.initWindows(windowSize);
        this.startResetTask();
    }

    @Override
    public boolean canPass() throws BlockException {
        lock.lock();
        if (passCount.get() > limitCount) {
            throw new BlockException();
        }
        windows[windowIndex].passCount.incrementAndGet();
        passCount.incrementAndGet();
        lock.unlock();
        return true;
    }

    private void initWindows(Integer windowSize) {
        windows = new Window[windowSize];
        for (int i = 0; i < windowSize; i++) {
            windows[i] = new Window();
        }
    }

    private ScheduledExecutorService scheduledExecutorService;
    private void startResetTask() {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(this, windowPeriod, windowPeriod, timeUnit);
    }

    @Override
    public void run() {
        // 获取当前窗口索引
        Integer curIndex = (windowIndex + 1) % windowSize;
        log.info("info_reset_task, curIndex = {}", curIndex);
        // 重置当前窗口索引通过数量，并获取上一次通过数量
        Integer count = windows[curIndex].passCount.getAndSet(0);
        windowIndex = curIndex;
        // 总通过数量 减去 当前窗口上次通过数量
        passCount.addAndGet(-count);
        log.info("info_reset_task, curOldCount = {}, passCount = {}, windows = {}", count, passCount, windows);
    }

    @Data
    class Window {
        private AtomicInteger passCount;
        public Window() {
            this.passCount = new AtomicInteger(0);
        }
    }

    public static void main(String[] args) {
        System.out.println(5 % 5);
        AtomicInteger integer = new AtomicInteger();
        int a = integer.incrementAndGet();
        System.out.println(a);
        a = integer.getAndSet(0);
        System.out.println(a);
        a = integer.getAndSet(0);
        System.out.println(a);
    }
}
