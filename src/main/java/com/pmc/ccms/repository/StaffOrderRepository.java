package com.pmc.ccms.repository;

import com.pmc.ccms.domain.StaffOrder;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the StaffOrder entity.
 */
@Repository
public interface StaffOrderRepository extends StaffOrderRepositoryWithBagRelationships, JpaRepository<StaffOrder, Long> {
    default Optional<StaffOrder> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<StaffOrder> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<StaffOrder> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
