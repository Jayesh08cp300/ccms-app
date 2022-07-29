package com.pmc.ccms.service;

import com.pmc.ccms.domain.StaffOrder;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link StaffOrder}.
 */
public interface StaffOrderService {
    /**
     * Save a staffOrder.
     *
     * @param staffOrder the entity to save.
     * @return the persisted entity.
     */
    StaffOrder save(StaffOrder staffOrder);

    /**
     * Updates a staffOrder.
     *
     * @param staffOrder the entity to update.
     * @return the persisted entity.
     */
    StaffOrder update(StaffOrder staffOrder);

    /**
     * Partially updates a staffOrder.
     *
     * @param staffOrder the entity to update partially.
     * @return the persisted entity.
     */
    Optional<StaffOrder> partialUpdate(StaffOrder staffOrder);

    /**
     * Get all the staffOrders.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<StaffOrder> findAll(Pageable pageable);

    /**
     * Get all the staffOrders with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<StaffOrder> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" staffOrder.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<StaffOrder> findOne(Long id);

    /**
     * Delete the "id" staffOrder.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
