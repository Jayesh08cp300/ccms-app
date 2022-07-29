package com.pmc.ccms.repository;

import com.pmc.ccms.domain.FoodServiceProvider;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FoodServiceProvider entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FoodServiceProviderRepository extends JpaRepository<FoodServiceProvider, Long> {}
