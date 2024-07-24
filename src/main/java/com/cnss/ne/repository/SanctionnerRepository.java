package com.cnss.ne.repository;

import com.cnss.ne.domain.Sanctionner;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Sanctionner entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SanctionnerRepository extends JpaRepository<Sanctionner, Long>, JpaSpecificationExecutor<Sanctionner> {}
