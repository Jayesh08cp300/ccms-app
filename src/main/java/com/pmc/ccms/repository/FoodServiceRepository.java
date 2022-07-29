package com.pmc.ccms.repository;

import com.pmc.ccms.domain.FoodService;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FoodService entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FoodServiceRepository extends JpaRepository<FoodService, Long> {}
