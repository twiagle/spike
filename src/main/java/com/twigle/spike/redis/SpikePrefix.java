package com.twigle.spike.redis;

public class SpikePrefix extends BasePrefix {

    private SpikePrefix(String prefix) {
        super(prefix);
    }

    public static SpikePrefix isGoodsSoldOut = new SpikePrefix("gso");
}
