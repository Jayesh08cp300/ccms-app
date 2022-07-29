package com.pmc.ccms.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.pmc.ccms.IntegrationTest;
import com.pmc.ccms.domain.FoodService;
import com.pmc.ccms.repository.FoodServiceRepository;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link FoodServiceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FoodServiceResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Float DEFAULT_RATE = 1F;
    private static final Float UPDATED_RATE = 2F;

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/food-services";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FoodServiceRepository foodServiceRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFoodServiceMockMvc;

    private FoodService foodService;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FoodService createEntity(EntityManager em) {
        FoodService foodService = new FoodService()
            .name(DEFAULT_NAME)
            .rate(DEFAULT_RATE)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE);
        return foodService;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FoodService createUpdatedEntity(EntityManager em) {
        FoodService foodService = new FoodService()
            .name(UPDATED_NAME)
            .rate(UPDATED_RATE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE);
        return foodService;
    }

    @BeforeEach
    public void initTest() {
        foodService = createEntity(em);
    }

    @Test
    @Transactional
    void createFoodService() throws Exception {
        int databaseSizeBeforeCreate = foodServiceRepository.findAll().size();
        // Create the FoodService
        restFoodServiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(foodService)))
            .andExpect(status().isCreated());

        // Validate the FoodService in the database
        List<FoodService> foodServiceList = foodServiceRepository.findAll();
        assertThat(foodServiceList).hasSize(databaseSizeBeforeCreate + 1);
        FoodService testFoodService = foodServiceList.get(foodServiceList.size() - 1);
        assertThat(testFoodService.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFoodService.getRate()).isEqualTo(DEFAULT_RATE);
        assertThat(testFoodService.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testFoodService.getEndDate()).isEqualTo(DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    void createFoodServiceWithExistingId() throws Exception {
        // Create the FoodService with an existing ID
        foodService.setId(1L);

        int databaseSizeBeforeCreate = foodServiceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFoodServiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(foodService)))
            .andExpect(status().isBadRequest());

        // Validate the FoodService in the database
        List<FoodService> foodServiceList = foodServiceRepository.findAll();
        assertThat(foodServiceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFoodServices() throws Exception {
        // Initialize the database
        foodServiceRepository.saveAndFlush(foodService);

        // Get all the foodServiceList
        restFoodServiceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(foodService.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].rate").value(hasItem(DEFAULT_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())));
    }

    @Test
    @Transactional
    void getFoodService() throws Exception {
        // Initialize the database
        foodServiceRepository.saveAndFlush(foodService);

        // Get the foodService
        restFoodServiceMockMvc
            .perform(get(ENTITY_API_URL_ID, foodService.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(foodService.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.rate").value(DEFAULT_RATE.doubleValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingFoodService() throws Exception {
        // Get the foodService
        restFoodServiceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFoodService() throws Exception {
        // Initialize the database
        foodServiceRepository.saveAndFlush(foodService);

        int databaseSizeBeforeUpdate = foodServiceRepository.findAll().size();

        // Update the foodService
        FoodService updatedFoodService = foodServiceRepository.findById(foodService.getId()).get();
        // Disconnect from session so that the updates on updatedFoodService are not directly saved in db
        em.detach(updatedFoodService);
        updatedFoodService.name(UPDATED_NAME).rate(UPDATED_RATE).startDate(UPDATED_START_DATE).endDate(UPDATED_END_DATE);

        restFoodServiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFoodService.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFoodService))
            )
            .andExpect(status().isOk());

        // Validate the FoodService in the database
        List<FoodService> foodServiceList = foodServiceRepository.findAll();
        assertThat(foodServiceList).hasSize(databaseSizeBeforeUpdate);
        FoodService testFoodService = foodServiceList.get(foodServiceList.size() - 1);
        assertThat(testFoodService.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFoodService.getRate()).isEqualTo(UPDATED_RATE);
        assertThat(testFoodService.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testFoodService.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void putNonExistingFoodService() throws Exception {
        int databaseSizeBeforeUpdate = foodServiceRepository.findAll().size();
        foodService.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFoodServiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, foodService.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(foodService))
            )
            .andExpect(status().isBadRequest());

        // Validate the FoodService in the database
        List<FoodService> foodServiceList = foodServiceRepository.findAll();
        assertThat(foodServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFoodService() throws Exception {
        int databaseSizeBeforeUpdate = foodServiceRepository.findAll().size();
        foodService.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFoodServiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(foodService))
            )
            .andExpect(status().isBadRequest());

        // Validate the FoodService in the database
        List<FoodService> foodServiceList = foodServiceRepository.findAll();
        assertThat(foodServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFoodService() throws Exception {
        int databaseSizeBeforeUpdate = foodServiceRepository.findAll().size();
        foodService.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFoodServiceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(foodService)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FoodService in the database
        List<FoodService> foodServiceList = foodServiceRepository.findAll();
        assertThat(foodServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFoodServiceWithPatch() throws Exception {
        // Initialize the database
        foodServiceRepository.saveAndFlush(foodService);

        int databaseSizeBeforeUpdate = foodServiceRepository.findAll().size();

        // Update the foodService using partial update
        FoodService partialUpdatedFoodService = new FoodService();
        partialUpdatedFoodService.setId(foodService.getId());

        partialUpdatedFoodService.rate(UPDATED_RATE);

        restFoodServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFoodService.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFoodService))
            )
            .andExpect(status().isOk());

        // Validate the FoodService in the database
        List<FoodService> foodServiceList = foodServiceRepository.findAll();
        assertThat(foodServiceList).hasSize(databaseSizeBeforeUpdate);
        FoodService testFoodService = foodServiceList.get(foodServiceList.size() - 1);
        assertThat(testFoodService.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFoodService.getRate()).isEqualTo(UPDATED_RATE);
        assertThat(testFoodService.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testFoodService.getEndDate()).isEqualTo(DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    void fullUpdateFoodServiceWithPatch() throws Exception {
        // Initialize the database
        foodServiceRepository.saveAndFlush(foodService);

        int databaseSizeBeforeUpdate = foodServiceRepository.findAll().size();

        // Update the foodService using partial update
        FoodService partialUpdatedFoodService = new FoodService();
        partialUpdatedFoodService.setId(foodService.getId());

        partialUpdatedFoodService.name(UPDATED_NAME).rate(UPDATED_RATE).startDate(UPDATED_START_DATE).endDate(UPDATED_END_DATE);

        restFoodServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFoodService.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFoodService))
            )
            .andExpect(status().isOk());

        // Validate the FoodService in the database
        List<FoodService> foodServiceList = foodServiceRepository.findAll();
        assertThat(foodServiceList).hasSize(databaseSizeBeforeUpdate);
        FoodService testFoodService = foodServiceList.get(foodServiceList.size() - 1);
        assertThat(testFoodService.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFoodService.getRate()).isEqualTo(UPDATED_RATE);
        assertThat(testFoodService.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testFoodService.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingFoodService() throws Exception {
        int databaseSizeBeforeUpdate = foodServiceRepository.findAll().size();
        foodService.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFoodServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, foodService.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(foodService))
            )
            .andExpect(status().isBadRequest());

        // Validate the FoodService in the database
        List<FoodService> foodServiceList = foodServiceRepository.findAll();
        assertThat(foodServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFoodService() throws Exception {
        int databaseSizeBeforeUpdate = foodServiceRepository.findAll().size();
        foodService.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFoodServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(foodService))
            )
            .andExpect(status().isBadRequest());

        // Validate the FoodService in the database
        List<FoodService> foodServiceList = foodServiceRepository.findAll();
        assertThat(foodServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFoodService() throws Exception {
        int databaseSizeBeforeUpdate = foodServiceRepository.findAll().size();
        foodService.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFoodServiceMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(foodService))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FoodService in the database
        List<FoodService> foodServiceList = foodServiceRepository.findAll();
        assertThat(foodServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFoodService() throws Exception {
        // Initialize the database
        foodServiceRepository.saveAndFlush(foodService);

        int databaseSizeBeforeDelete = foodServiceRepository.findAll().size();

        // Delete the foodService
        restFoodServiceMockMvc
            .perform(delete(ENTITY_API_URL_ID, foodService.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FoodService> foodServiceList = foodServiceRepository.findAll();
        assertThat(foodServiceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
