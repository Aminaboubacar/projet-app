package com.cnss.ne.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DisponibiliteTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Disponibilite getDisponibiliteSample1() {
        return new Disponibilite().id(1L).code("code1");
    }

    public static Disponibilite getDisponibiliteSample2() {
        return new Disponibilite().id(2L).code("code2");
    }

    public static Disponibilite getDisponibiliteRandomSampleGenerator() {
        return new Disponibilite().id(longCount.incrementAndGet()).code(UUID.randomUUID().toString());
    }
}
