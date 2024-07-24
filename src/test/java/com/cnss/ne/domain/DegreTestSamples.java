package com.cnss.ne.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DegreTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Degre getDegreSample1() {
        return new Degre().id(1L).code("code1").libelle("libelle1");
    }

    public static Degre getDegreSample2() {
        return new Degre().id(2L).code("code2").libelle("libelle2");
    }

    public static Degre getDegreRandomSampleGenerator() {
        return new Degre().id(longCount.incrementAndGet()).code(UUID.randomUUID().toString()).libelle(UUID.randomUUID().toString());
    }
}
