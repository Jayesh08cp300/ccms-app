package com.pmc.ccms.repository;

import com.pmc.ccms.domain.StaffOrder;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface StaffOrderRepositoryWithBagRelationships {
    Optional<StaffOrder> fetchBagRelationships(Optional<StaffOrder> staffOrder);

    List<StaffOrder> fetchBagRelationships(List<StaffOrder> staffOrders);

    Page<StaffOrder> fetchBagRelationships(Page<StaffOrder> staffOrders);
}
