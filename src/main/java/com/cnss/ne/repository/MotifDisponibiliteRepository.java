package com.cnss.ne.repository;

import com.cnss.ne.domain.MotifDisponibilite;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MotifDisponibilite entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MotifDisponibiliteRepository
    extends JpaRepository<MotifDisponibilite, Long>, JpaSpecificationExecutor<MotifDisponibilite> {}
