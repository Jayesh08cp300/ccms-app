package com.pmc.ccms.service;

import com.pmc.ccms.domain.FoodServiceProvider;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link FoodServiceProvider}.
 */
public interface FoodServiceProviderService {
    /**
     * Save a foodServiceProvider.
     *
     * @param foodServiceProvider the entity to save.
     * @return the persisted entity.
     */
    FoodServiceProvider save(FoodServiceProvider foodServiceProvider);

    /**
     * Updates a foodServiceProvider.
     *
     * @param foodServiceProvider the entity to update.
     * @return the persisted entity.
     */
    FoodServiceProvider update(FoodServiceProvider foodServiceProvider);

    /**
     * Partially updates a foodServiceProvider.
     *
     * @param foodServiceProvider the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FoodServiceProvider> partialUpdate(FoodServiceProvider foodServiceProvider);

    /**
     * Get all the foodServiceProviders.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FoodServiceProvider> findAll(Pageable pageable);

    /**
     * Get the "id" foodServiceProvider.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FoodServiceProvider> findOne(Long id);

    /**
     * Delete the "id" foodServiceProvider.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
