package com.twigle.spike.redis;

public interface KeyPrefix {
    public int expireSeconds();
    public String getPrefix();
}
