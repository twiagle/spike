package com.twigle.spike.redis;

public abstract class BasePrefix implements KeyPrefix{
    private int expiredSeconds;
    private String prefix;

    @Override
    public int expireSeconds() {
        return expiredSeconds;
    }

    @Override
    public String getPrefix() {
        return this.getClass().getSimpleName()+":"+prefix;//class can seperate namespace,prefix can seperate internal
    }

    //construction, never expire, sons will
    public BasePrefix(String prefix) {
        this(0, prefix);
    }

    public BasePrefix(int expiredSeconds, String prefix) {
        this.expiredSeconds = expiredSeconds;
        this.prefix = prefix;
    }
}
