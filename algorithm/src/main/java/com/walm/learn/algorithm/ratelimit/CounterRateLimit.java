package com.walm.learn.algorithm.ratelimit;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>CounterRateLimit</p>
 *
 * @author wangjn
 * @since 2019-09-30
 */
public class CounterRateLimit implements RateLimit , Runnable {

    private Integer limitCount;

    private AtomicInteger passCount;

    private long period;

    private TimeUnit timeUnit;

    private ScheduledExecutorService scheduledExecutorService;


    public CounterRateLimit(Integer limitCount) {
        this(limitCount, 1000, TimeUnit.MILLISECONDS);
    }

    public CounterRateLimit(Integer limitCount, long period, TimeUnit timeUnit) {
        this.limitCount = limitCount;
        this.period = period;
        this.timeUnit = timeUnit;
        passCount = new AtomicInteger(0);
        this.startResetTask();
    }

    @Override
    public boolean canPass() throws BlockException {
        if (passCount.incrementAndGet() > limitCount) {
            throw new BlockException();
        }
        return true;
    }

    public void startResetTask() {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(this, 0, period, timeUnit);
    }

    @Override
    public void run() {
        passCount.set(0);
    }



    //------------------------------ test ------------------------------//

    public static void main(String[] args) {

        RateLimit rateLimit = new CounterRateLimit(20);
        for (int i = 0; i<50; i++) {
            try {
                rateLimit.canPass();
                System.out.println("request pass !!!");
            } catch (BlockException e) {
               System.out.println("request block !!!");
            }
        }
    }
}
