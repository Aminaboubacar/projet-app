package com.cnss.ne.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MiseadispositionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Miseadisposition getMiseadispositionSample1() {
        return new Miseadisposition().id(1L).code("code1").organisme("organisme1").sensMouvement("sensMouvement1");
    }

    public static Miseadisposition getMiseadispositionSample2() {
        return new Miseadisposition().id(2L).code("code2").organisme("organisme2").sensMouvement("sensMouvement2");
    }

    public static Miseadisposition getMiseadispositionRandomSampleGenerator() {
        return new Miseadisposition()
            .id(longCount.incrementAndGet())
            .code(UUID.randomUUID().toString())
            .organisme(UUID.randomUUID().toString())
            .sensMouvement(UUID.randomUUID().toString());
    }
}
