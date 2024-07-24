package com.cnss.ne.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SanctionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Sanction getSanctionSample1() {
        return new Sanction().id(1L).code("code1").libelle("libelle1");
    }

    public static Sanction getSanctionSample2() {
        return new Sanction().id(2L).code("code2").libelle("libelle2");
    }

    public static Sanction getSanctionRandomSampleGenerator() {
        return new Sanction().id(longCount.incrementAndGet()).code(UUID.randomUUID().toString()).libelle(UUID.randomUUID().toString());
    }
}
