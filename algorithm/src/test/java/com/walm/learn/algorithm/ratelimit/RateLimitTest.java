package com.walm.learn.algorithm.ratelimit;

import org.junit.Test;

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
}
