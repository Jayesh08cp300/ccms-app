package com.pmc.ccms.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.pmc.ccms.IntegrationTest;
import com.pmc.ccms.domain.MenuItem;
import com.pmc.ccms.repository.MenuItemRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link MenuItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MenuItemResourceIT {

    private static final Boolean DEFAULT_LIMITED = false;
    private static final Boolean UPDATED_LIMITED = true;

    private static final String ENTITY_API_URL = "/api/menu-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMenuItemMockMvc;

    private MenuItem menuItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MenuItem createEntity(EntityManager em) {
        MenuItem menuItem = new MenuItem().limited(DEFAULT_LIMITED);
        return menuItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MenuItem createUpdatedEntity(EntityManager em) {
        MenuItem menuItem = new MenuItem().limited(UPDATED_LIMITED);
        return menuItem;
    }

    @BeforeEach
    public void initTest() {
        menuItem = createEntity(em);
    }

    @Test
    @Transactional
    void createMenuItem() throws Exception {
        int databaseSizeBeforeCreate = menuItemRepository.findAll().size();
        // Create the MenuItem
        restMenuItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(menuItem)))
            .andExpect(status().isCreated());

        // Validate the MenuItem in the database
        List<MenuItem> menuItemList = menuItemRepository.findAll();
        assertThat(menuItemList).hasSize(databaseSizeBeforeCreate + 1);
        MenuItem testMenuItem = menuItemList.get(menuItemList.size() - 1);
        assertThat(testMenuItem.getLimited()).isEqualTo(DEFAULT_LIMITED);
    }

    @Test
    @Transactional
    void createMenuItemWithExistingId() throws Exception {
        // Create the MenuItem with an existing ID
        menuItem.setId(1L);

        int databaseSizeBeforeCreate = menuItemRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMenuItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(menuItem)))
            .andExpect(status().isBadRequest());

        // Validate the MenuItem in the database
        List<MenuItem> menuItemList = menuItemRepository.findAll();
        assertThat(menuItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMenuItems() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get all the menuItemList
        restMenuItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(menuItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].limited").value(hasItem(DEFAULT_LIMITED.booleanValue())));
    }

    @Test
    @Transactional
    void getMenuItem() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get the menuItem
        restMenuItemMockMvc
            .perform(get(ENTITY_API_URL_ID, menuItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(menuItem.getId().intValue()))
            .andExpect(jsonPath("$.limited").value(DEFAULT_LIMITED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingMenuItem() throws Exception {
        // Get the menuItem
        restMenuItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMenuItem() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        int databaseSizeBeforeUpdate = menuItemRepository.findAll().size();

        // Update the menuItem
        MenuItem updatedMenuItem = menuItemRepository.findById(menuItem.getId()).get();
        // Disconnect from session so that the updates on updatedMenuItem are not directly saved in db
        em.detach(updatedMenuItem);
        updatedMenuItem.limited(UPDATED_LIMITED);

        restMenuItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMenuItem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMenuItem))
            )
            .andExpect(status().isOk());

        // Validate the MenuItem in the database
        List<MenuItem> menuItemList = menuItemRepository.findAll();
        assertThat(menuItemList).hasSize(databaseSizeBeforeUpdate);
        MenuItem testMenuItem = menuItemList.get(menuItemList.size() - 1);
        assertThat(testMenuItem.getLimited()).isEqualTo(UPDATED_LIMITED);
    }

    @Test
    @Transactional
    void putNonExistingMenuItem() throws Exception {
        int databaseSizeBeforeUpdate = menuItemRepository.findAll().size();
        menuItem.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMenuItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, menuItem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(menuItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the MenuItem in the database
        List<MenuItem> menuItemList = menuItemRepository.findAll();
        assertThat(menuItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMenuItem() throws Exception {
        int databaseSizeBeforeUpdate = menuItemRepository.findAll().size();
        menuItem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMenuItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(menuItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the MenuItem in the database
        List<MenuItem> menuItemList = menuItemRepository.findAll();
        assertThat(menuItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMenuItem() throws Exception {
        int databaseSizeBeforeUpdate = menuItemRepository.findAll().size();
        menuItem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMenuItemMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(menuItem)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MenuItem in the database
        List<MenuItem> menuItemList = menuItemRepository.findAll();
        assertThat(menuItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMenuItemWithPatch() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        int databaseSizeBeforeUpdate = menuItemRepository.findAll().size();

        // Update the menuItem using partial update
        MenuItem partialUpdatedMenuItem = new MenuItem();
        partialUpdatedMenuItem.setId(menuItem.getId());

        partialUpdatedMenuItem.limited(UPDATED_LIMITED);

        restMenuItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMenuItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMenuItem))
            )
            .andExpect(status().isOk());

        // Validate the MenuItem in the database
        List<MenuItem> menuItemList = menuItemRepository.findAll();
        assertThat(menuItemList).hasSize(databaseSizeBeforeUpdate);
        MenuItem testMenuItem = menuItemList.get(menuItemList.size() - 1);
        assertThat(testMenuItem.getLimited()).isEqualTo(UPDATED_LIMITED);
    }

    @Test
    @Transactional
    void fullUpdateMenuItemWithPatch() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        int databaseSizeBeforeUpdate = menuItemRepository.findAll().size();

        // Update the menuItem using partial update
        MenuItem partialUpdatedMenuItem = new MenuItem();
        partialUpdatedMenuItem.setId(menuItem.getId());

        partialUpdatedMenuItem.limited(UPDATED_LIMITED);

        restMenuItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMenuItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMenuItem))
            )
            .andExpect(status().isOk());

        // Validate the MenuItem in the database
        List<MenuItem> menuItemList = menuItemRepository.findAll();
        assertThat(menuItemList).hasSize(databaseSizeBeforeUpdate);
        MenuItem testMenuItem = menuItemList.get(menuItemList.size() - 1);
        assertThat(testMenuItem.getLimited()).isEqualTo(UPDATED_LIMITED);
    }

    @Test
    @Transactional
    void patchNonExistingMenuItem() throws Exception {
        int databaseSizeBeforeUpdate = menuItemRepository.findAll().size();
        menuItem.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMenuItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, menuItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(menuItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the MenuItem in the database
        List<MenuItem> menuItemList = menuItemRepository.findAll();
        assertThat(menuItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMenuItem() throws Exception {
        int databaseSizeBeforeUpdate = menuItemRepository.findAll().size();
        menuItem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMenuItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(menuItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the MenuItem in the database
        List<MenuItem> menuItemList = menuItemRepository.findAll();
        assertThat(menuItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMenuItem() throws Exception {
        int databaseSizeBeforeUpdate = menuItemRepository.findAll().size();
        menuItem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMenuItemMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(menuItem)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MenuItem in the database
        List<MenuItem> menuItemList = menuItemRepository.findAll();
        assertThat(menuItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMenuItem() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        int databaseSizeBeforeDelete = menuItemRepository.findAll().size();

        // Delete the menuItem
        restMenuItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, menuItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MenuItem> menuItemList = menuItemRepository.findAll();
        assertThat(menuItemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
