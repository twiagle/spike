package com.twigle.spike.redis;

public class SpikeUserPrefix extends BasePrefix{
    public static final int TOKEN_EXPIRE = 3600 * 24 * 2;
    public static SpikeUserPrefix Prefix = new SpikeUserPrefix(TOKEN_EXPIRE, "token");

    private SpikeUserPrefix(int expiredSeconds, String prefix) {
        super(expiredSeconds,prefix);
    }

}
