package com.pmc.ccms.service;

import com.pmc.ccms.domain.FoodService;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link FoodService}.
 */
public interface FoodServiceService {
    /**
     * Save a foodService.
     *
     * @param foodService the entity to save.
     * @return the persisted entity.
     */
    FoodService save(FoodService foodService);

    /**
     * Updates a foodService.
     *
     * @param foodService the entity to update.
     * @return the persisted entity.
     */
    FoodService update(FoodService foodService);

    /**
     * Partially updates a foodService.
     *
     * @param foodService the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FoodService> partialUpdate(FoodService foodService);

    /**
     * Get all the foodServices.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FoodService> findAll(Pageable pageable);

    /**
     * Get the "id" foodService.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FoodService> findOne(Long id);

    /**
     * Delete the "id" foodService.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
