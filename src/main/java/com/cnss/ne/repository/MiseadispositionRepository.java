package com.cnss.ne.repository;

import com.cnss.ne.domain.Miseadisposition;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Miseadisposition entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MiseadispositionRepository extends JpaRepository<Miseadisposition, Long>, JpaSpecificationExecutor<Miseadisposition> {}
