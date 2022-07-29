package com.pmc.ccms.repository;

import com.pmc.ccms.domain.MenuItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the MenuItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {}
