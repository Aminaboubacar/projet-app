package com.cnss.ne.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DemandeDexplicationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static DemandeDexplication getDemandeDexplicationSample1() {
        return new DemandeDexplication().id(1L).code("code1").objet("objet1").appDg("appDg1");
    }

    public static DemandeDexplication getDemandeDexplicationSample2() {
        return new DemandeDexplication().id(2L).code("code2").objet("objet2").appDg("appDg2");
    }

    public static DemandeDexplication getDemandeDexplicationRandomSampleGenerator() {
        return new DemandeDexplication()
            .id(longCount.incrementAndGet())
            .code(UUID.randomUUID().toString())
            .objet(UUID.randomUUID().toString())
            .appDg(UUID.randomUUID().toString());
    }
}
