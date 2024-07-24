package com.cnss.ne.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PosteTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Poste getPosteSample1() {
        return new Poste().id(1L).code("code1").libelle("libelle1");
    }

    public static Poste getPosteSample2() {
        return new Poste().id(2L).code("code2").libelle("libelle2");
    }

    public static Poste getPosteRandomSampleGenerator() {
        return new Poste().id(longCount.incrementAndGet()).code(UUID.randomUUID().toString()).libelle(UUID.randomUUID().toString());
    }
}
