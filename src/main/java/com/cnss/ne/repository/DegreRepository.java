package com.cnss.ne.repository;

import com.cnss.ne.domain.Degre;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Degre entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DegreRepository extends JpaRepository<Degre, Long>, JpaSpecificationExecutor<Degre> {}
