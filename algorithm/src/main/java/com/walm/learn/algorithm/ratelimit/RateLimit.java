package com.walm.learn.algorithm.ratelimit;

/**
 * <p>RateLimit</p>
 *
 * @author wangjn
 * @since 2019-09-30
 */
public interface RateLimit {

    /**
     * can pass
     */
    boolean canPass() throws BlockException;
}
