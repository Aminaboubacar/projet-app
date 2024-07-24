package com.cnss.ne.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class SanctionnerTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Sanctionner getSanctionnerSample1() {
        return new Sanctionner().id(1L);
    }

    public static Sanctionner getSanctionnerSample2() {
        return new Sanctionner().id(2L);
    }

    public static Sanctionner getSanctionnerRandomSampleGenerator() {
        return new Sanctionner().id(longCount.incrementAndGet());
    }
}
