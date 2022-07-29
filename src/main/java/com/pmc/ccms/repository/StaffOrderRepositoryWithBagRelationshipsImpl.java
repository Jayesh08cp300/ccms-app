package com.pmc.ccms.repository;

import com.pmc.ccms.domain.StaffOrder;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class StaffOrderRepositoryWithBagRelationshipsImpl implements StaffOrderRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<StaffOrder> fetchBagRelationships(Optional<StaffOrder> staffOrder) {
        return staffOrder.map(this::fetchUsers);
    }

    @Override
    public Page<StaffOrder> fetchBagRelationships(Page<StaffOrder> staffOrders) {
        return new PageImpl<>(fetchBagRelationships(staffOrders.getContent()), staffOrders.getPageable(), staffOrders.getTotalElements());
    }

    @Override
    public List<StaffOrder> fetchBagRelationships(List<StaffOrder> staffOrders) {
        return Optional.of(staffOrders).map(this::fetchUsers).orElse(Collections.emptyList());
    }

    StaffOrder fetchUsers(StaffOrder result) {
        return entityManager
            .createQuery(
                "select staffOrder from StaffOrder staffOrder left join fetch staffOrder.users where staffOrder is :staffOrder",
                StaffOrder.class
            )
            .setParameter("staffOrder", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<StaffOrder> fetchUsers(List<StaffOrder> staffOrders) {
        return entityManager
            .createQuery(
                "select distinct staffOrder from StaffOrder staffOrder left join fetch staffOrder.users where staffOrder in :staffOrders",
                StaffOrder.class
            )
            .setParameter("staffOrders", staffOrders)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
