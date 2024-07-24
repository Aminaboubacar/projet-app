package com.cnss.ne.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AgentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Agent getAgentSample1() {
        return new Agent()
            .id(1L)
            .matricule("matricule1")
            .nom("nom1")
            .prenom("prenom1")
            .lieuNaissance("lieuNaissance1")
            .nationalite("nationalite1")
            .telephone("telephone1")
            .adresse("adresse1")
            .causeDece("causeDece1");
    }

    public static Agent getAgentSample2() {
        return new Agent()
            .id(2L)
            .matricule("matricule2")
            .nom("nom2")
            .prenom("prenom2")
            .lieuNaissance("lieuNaissance2")
            .nationalite("nationalite2")
            .telephone("telephone2")
            .adresse("adresse2")
            .causeDece("causeDece2");
    }

    public static Agent getAgentRandomSampleGenerator() {
        return new Agent()
            .id(longCount.incrementAndGet())
            .matricule(UUID.randomUUID().toString())
            .nom(UUID.randomUUID().toString())
            .prenom(UUID.randomUUID().toString())
            .lieuNaissance(UUID.randomUUID().toString())
            .nationalite(UUID.randomUUID().toString())
            .telephone(UUID.randomUUID().toString())
            .adresse(UUID.randomUUID().toString())
            .causeDece(UUID.randomUUID().toString());
    }
}
