package com.walm.learn.algorithm.ratelimit;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <p>RateLimitTest</p>
 *
 * @author wangjn
 * @since 2019-09-30
 */
public class RateLimitTest {

    @Test
    public void counterRateLimitTest() throws InterruptedException {
        RateLimit rateLimit = new CounterRateLimit(20);
        for (int i = 0; i<50; i++) {
            try {
                rateLimit.canPass();
                System.out.println("request pass !!!");
                Thread.sleep(30);
            } catch (BlockException e) {
                System.out.println("request block !!!");
                Thread.sleep(30);
            }
        }
    }


    @Test
    public void slidingWindowRateLimitTest() throws InterruptedException {
        RateLimit rateLimit = new SlidingWindowRateLimit(20);
        for (int i = 0; i<80; i++) {
            try {
                rateLimit.canPass();
                System.out.println("request pass !!!");
                Thread.sleep(40);
            } catch (BlockException e) {
                System.out.println("request block !!!");
                Thread.sleep(40);
            }
        }
    }

    @Test
    public void LeakyBucketRateLimitTest() throws InterruptedException {
        RateLimit rateLimit = new LeakyBucketRateLimit(10, 5);
        ExecutorService threadPool = Executors.newCachedThreadPool();
        for (int i = 0; i< 20; i++) {
            threadPool.execute(() -> {
                try {
                    rateLimit.canPass();
                    System.out.println("request pass !!!");
                } catch (BlockException e) {
                    System.out.println("request block !!!");
                }
            });
            Thread.sleep(10);
        }
        Thread.sleep(60 * 1000);
    }

    @Test
    public void TokenBucketRateLimitTest() throws InterruptedException {
        RateLimit rateLimit = new TokenBucketRateLimit(10, 20);
        ExecutorService threadPool = Executors.newCachedThreadPool();
        for (int i = 0; i< 20; i++) {
            threadPool.execute(() -> {
                try {
                    rateLimit.canPass();
                    System.out.println("request pass !!!");
                } catch (BlockException e) {
                    System.out.println("request block !!!");
                }
            });
            Thread.sleep(10);
        }
        Thread.sleep(60 * 1000);
    }
}
