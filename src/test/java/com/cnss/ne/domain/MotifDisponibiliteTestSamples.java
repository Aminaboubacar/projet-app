package com.cnss.ne.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MotifDisponibiliteTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static MotifDisponibilite getMotifDisponibiliteSample1() {
        return new MotifDisponibilite().id(1L).code("code1").libelle("libelle1");
    }

    public static MotifDisponibilite getMotifDisponibiliteSample2() {
        return new MotifDisponibilite().id(2L).code("code2").libelle("libelle2");
    }

    public static MotifDisponibilite getMotifDisponibiliteRandomSampleGenerator() {
        return new MotifDisponibilite()
            .id(longCount.incrementAndGet())
            .code(UUID.randomUUID().toString())
            .libelle(UUID.randomUUID().toString());
    }
}
