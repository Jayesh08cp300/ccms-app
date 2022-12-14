package com.pmc.ccms.service;

import com.pmc.ccms.domain.MenuItem;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link MenuItem}.
 */
public interface MenuItemService {
    /**
     * Save a menuItem.
     *
     * @param menuItem the entity to save.
     * @return the persisted entity.
     */
    MenuItem save(MenuItem menuItem);

    /**
     * Updates a menuItem.
     *
     * @param menuItem the entity to update.
     * @return the persisted entity.
     */
    MenuItem update(MenuItem menuItem);

    /**
     * Partially updates a menuItem.
     *
     * @param menuItem the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MenuItem> partialUpdate(MenuItem menuItem);

    /**
     * Get all the menuItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MenuItem> findAll(Pageable pageable);

    /**
     * Get the "id" menuItem.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MenuItem> findOne(Long id);

    /**
     * Delete the "id" menuItem.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
