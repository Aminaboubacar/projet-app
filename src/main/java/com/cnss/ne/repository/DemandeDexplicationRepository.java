package com.cnss.ne.repository;

import com.cnss.ne.domain.DemandeDexplication;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DemandeDexplication entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DemandeDexplicationRepository
    extends JpaRepository<DemandeDexplication, Long>, JpaSpecificationExecutor<DemandeDexplication> {}
