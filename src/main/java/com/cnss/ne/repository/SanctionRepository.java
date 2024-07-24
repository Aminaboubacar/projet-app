package com.cnss.ne.repository;

import com.cnss.ne.domain.Sanction;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Sanction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SanctionRepository extends JpaRepository<Sanction, Long>, JpaSpecificationExecutor<Sanction> {}
