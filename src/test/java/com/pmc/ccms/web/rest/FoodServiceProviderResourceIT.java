package com.pmc.ccms.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.pmc.ccms.IntegrationTest;
import com.pmc.ccms.domain.FoodServiceProvider;
import com.pmc.ccms.repository.FoodServiceProviderRepository;
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
 * Integration tests for the {@link FoodServiceProviderResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FoodServiceProviderResourceIT {

    private static final String DEFAULT_FULL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FULL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DOC_PROOF_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DOC_PROOF_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DOC_PROOF_NO = "AAAAAAAAAA";
    private static final String UPDATED_DOC_PROOF_NO = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_NO = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_NO = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_ADDRESS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/food-service-providers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FoodServiceProviderRepository foodServiceProviderRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFoodServiceProviderMockMvc;

    private FoodServiceProvider foodServiceProvider;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FoodServiceProvider createEntity(EntityManager em) {
        FoodServiceProvider foodServiceProvider = new FoodServiceProvider()
            .fullName(DEFAULT_FULL_NAME)
            .docProofName(DEFAULT_DOC_PROOF_NAME)
            .docProofNo(DEFAULT_DOC_PROOF_NO)
            .address(DEFAULT_ADDRESS)
            .contactNo(DEFAULT_CONTACT_NO)
            .emailAddress(DEFAULT_EMAIL_ADDRESS);
        return foodServiceProvider;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FoodServiceProvider createUpdatedEntity(EntityManager em) {
        FoodServiceProvider foodServiceProvider = new FoodServiceProvider()
            .fullName(UPDATED_FULL_NAME)
            .docProofName(UPDATED_DOC_PROOF_NAME)
            .docProofNo(UPDATED_DOC_PROOF_NO)
            .address(UPDATED_ADDRESS)
            .contactNo(UPDATED_CONTACT_NO)
            .emailAddress(UPDATED_EMAIL_ADDRESS);
        return foodServiceProvider;
    }

    @BeforeEach
    public void initTest() {
        foodServiceProvider = createEntity(em);
    }

    @Test
    @Transactional
    void createFoodServiceProvider() throws Exception {
        int databaseSizeBeforeCreate = foodServiceProviderRepository.findAll().size();
        // Create the FoodServiceProvider
        restFoodServiceProviderMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(foodServiceProvider))
            )
            .andExpect(status().isCreated());

        // Validate the FoodServiceProvider in the database
        List<FoodServiceProvider> foodServiceProviderList = foodServiceProviderRepository.findAll();
        assertThat(foodServiceProviderList).hasSize(databaseSizeBeforeCreate + 1);
        FoodServiceProvider testFoodServiceProvider = foodServiceProviderList.get(foodServiceProviderList.size() - 1);
        assertThat(testFoodServiceProvider.getFullName()).isEqualTo(DEFAULT_FULL_NAME);
        assertThat(testFoodServiceProvider.getDocProofName()).isEqualTo(DEFAULT_DOC_PROOF_NAME);
        assertThat(testFoodServiceProvider.getDocProofNo()).isEqualTo(DEFAULT_DOC_PROOF_NO);
        assertThat(testFoodServiceProvider.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testFoodServiceProvider.getContactNo()).isEqualTo(DEFAULT_CONTACT_NO);
        assertThat(testFoodServiceProvider.getEmailAddress()).isEqualTo(DEFAULT_EMAIL_ADDRESS);
    }

    @Test
    @Transactional
    void createFoodServiceProviderWithExistingId() throws Exception {
        // Create the FoodServiceProvider with an existing ID
        foodServiceProvider.setId(1L);

        int databaseSizeBeforeCreate = foodServiceProviderRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFoodServiceProviderMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(foodServiceProvider))
            )
            .andExpect(status().isBadRequest());

        // Validate the FoodServiceProvider in the database
        List<FoodServiceProvider> foodServiceProviderList = foodServiceProviderRepository.findAll();
        assertThat(foodServiceProviderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFoodServiceProviders() throws Exception {
        // Initialize the database
        foodServiceProviderRepository.saveAndFlush(foodServiceProvider);

        // Get all the foodServiceProviderList
        restFoodServiceProviderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(foodServiceProvider.getId().intValue())))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].docProofName").value(hasItem(DEFAULT_DOC_PROOF_NAME)))
            .andExpect(jsonPath("$.[*].docProofNo").value(hasItem(DEFAULT_DOC_PROOF_NO)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].contactNo").value(hasItem(DEFAULT_CONTACT_NO)))
            .andExpect(jsonPath("$.[*].emailAddress").value(hasItem(DEFAULT_EMAIL_ADDRESS)));
    }

    @Test
    @Transactional
    void getFoodServiceProvider() throws Exception {
        // Initialize the database
        foodServiceProviderRepository.saveAndFlush(foodServiceProvider);

        // Get the foodServiceProvider
        restFoodServiceProviderMockMvc
            .perform(get(ENTITY_API_URL_ID, foodServiceProvider.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(foodServiceProvider.getId().intValue()))
            .andExpect(jsonPath("$.fullName").value(DEFAULT_FULL_NAME))
            .andExpect(jsonPath("$.docProofName").value(DEFAULT_DOC_PROOF_NAME))
            .andExpect(jsonPath("$.docProofNo").value(DEFAULT_DOC_PROOF_NO))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.contactNo").value(DEFAULT_CONTACT_NO))
            .andExpect(jsonPath("$.emailAddress").value(DEFAULT_EMAIL_ADDRESS));
    }

    @Test
    @Transactional
    void getNonExistingFoodServiceProvider() throws Exception {
        // Get the foodServiceProvider
        restFoodServiceProviderMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFoodServiceProvider() throws Exception {
        // Initialize the database
        foodServiceProviderRepository.saveAndFlush(foodServiceProvider);

        int databaseSizeBeforeUpdate = foodServiceProviderRepository.findAll().size();

        // Update the foodServiceProvider
        FoodServiceProvider updatedFoodServiceProvider = foodServiceProviderRepository.findById(foodServiceProvider.getId()).get();
        // Disconnect from session so that the updates on updatedFoodServiceProvider are not directly saved in db
        em.detach(updatedFoodServiceProvider);
        updatedFoodServiceProvider
            .fullName(UPDATED_FULL_NAME)
            .docProofName(UPDATED_DOC_PROOF_NAME)
            .docProofNo(UPDATED_DOC_PROOF_NO)
            .address(UPDATED_ADDRESS)
            .contactNo(UPDATED_CONTACT_NO)
            .emailAddress(UPDATED_EMAIL_ADDRESS);

        restFoodServiceProviderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFoodServiceProvider.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFoodServiceProvider))
            )
            .andExpect(status().isOk());

        // Validate the FoodServiceProvider in the database
        List<FoodServiceProvider> foodServiceProviderList = foodServiceProviderRepository.findAll();
        assertThat(foodServiceProviderList).hasSize(databaseSizeBeforeUpdate);
        FoodServiceProvider testFoodServiceProvider = foodServiceProviderList.get(foodServiceProviderList.size() - 1);
        assertThat(testFoodServiceProvider.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testFoodServiceProvider.getDocProofName()).isEqualTo(UPDATED_DOC_PROOF_NAME);
        assertThat(testFoodServiceProvider.getDocProofNo()).isEqualTo(UPDATED_DOC_PROOF_NO);
        assertThat(testFoodServiceProvider.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testFoodServiceProvider.getContactNo()).isEqualTo(UPDATED_CONTACT_NO);
        assertThat(testFoodServiceProvider.getEmailAddress()).isEqualTo(UPDATED_EMAIL_ADDRESS);
    }

    @Test
    @Transactional
    void putNonExistingFoodServiceProvider() throws Exception {
        int databaseSizeBeforeUpdate = foodServiceProviderRepository.findAll().size();
        foodServiceProvider.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFoodServiceProviderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, foodServiceProvider.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(foodServiceProvider))
            )
            .andExpect(status().isBadRequest());

        // Validate the FoodServiceProvider in the database
        List<FoodServiceProvider> foodServiceProviderList = foodServiceProviderRepository.findAll();
        assertThat(foodServiceProviderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFoodServiceProvider() throws Exception {
        int databaseSizeBeforeUpdate = foodServiceProviderRepository.findAll().size();
        foodServiceProvider.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFoodServiceProviderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(foodServiceProvider))
            )
            .andExpect(status().isBadRequest());

        // Validate the FoodServiceProvider in the database
        List<FoodServiceProvider> foodServiceProviderList = foodServiceProviderRepository.findAll();
        assertThat(foodServiceProviderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFoodServiceProvider() throws Exception {
        int databaseSizeBeforeUpdate = foodServiceProviderRepository.findAll().size();
        foodServiceProvider.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFoodServiceProviderMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(foodServiceProvider))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FoodServiceProvider in the database
        List<FoodServiceProvider> foodServiceProviderList = foodServiceProviderRepository.findAll();
        assertThat(foodServiceProviderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFoodServiceProviderWithPatch() throws Exception {
        // Initialize the database
        foodServiceProviderRepository.saveAndFlush(foodServiceProvider);

        int databaseSizeBeforeUpdate = foodServiceProviderRepository.findAll().size();

        // Update the foodServiceProvider using partial update
        FoodServiceProvider partialUpdatedFoodServiceProvider = new FoodServiceProvider();
        partialUpdatedFoodServiceProvider.setId(foodServiceProvider.getId());

        partialUpdatedFoodServiceProvider.docProofName(UPDATED_DOC_PROOF_NAME).contactNo(UPDATED_CONTACT_NO);

        restFoodServiceProviderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFoodServiceProvider.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFoodServiceProvider))
            )
            .andExpect(status().isOk());

        // Validate the FoodServiceProvider in the database
        List<FoodServiceProvider> foodServiceProviderList = foodServiceProviderRepository.findAll();
        assertThat(foodServiceProviderList).hasSize(databaseSizeBeforeUpdate);
        FoodServiceProvider testFoodServiceProvider = foodServiceProviderList.get(foodServiceProviderList.size() - 1);
        assertThat(testFoodServiceProvider.getFullName()).isEqualTo(DEFAULT_FULL_NAME);
        assertThat(testFoodServiceProvider.getDocProofName()).isEqualTo(UPDATED_DOC_PROOF_NAME);
        assertThat(testFoodServiceProvider.getDocProofNo()).isEqualTo(DEFAULT_DOC_PROOF_NO);
        assertThat(testFoodServiceProvider.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testFoodServiceProvider.getContactNo()).isEqualTo(UPDATED_CONTACT_NO);
        assertThat(testFoodServiceProvider.getEmailAddress()).isEqualTo(DEFAULT_EMAIL_ADDRESS);
    }

    @Test
    @Transactional
    void fullUpdateFoodServiceProviderWithPatch() throws Exception {
        // Initialize the database
        foodServiceProviderRepository.saveAndFlush(foodServiceProvider);

        int databaseSizeBeforeUpdate = foodServiceProviderRepository.findAll().size();

        // Update the foodServiceProvider using partial update
        FoodServiceProvider partialUpdatedFoodServiceProvider = new FoodServiceProvider();
        partialUpdatedFoodServiceProvider.setId(foodServiceProvider.getId());

        partialUpdatedFoodServiceProvider
            .fullName(UPDATED_FULL_NAME)
            .docProofName(UPDATED_DOC_PROOF_NAME)
            .docProofNo(UPDATED_DOC_PROOF_NO)
            .address(UPDATED_ADDRESS)
            .contactNo(UPDATED_CONTACT_NO)
            .emailAddress(UPDATED_EMAIL_ADDRESS);

        restFoodServiceProviderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFoodServiceProvider.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFoodServiceProvider))
            )
            .andExpect(status().isOk());

        // Validate the FoodServiceProvider in the database
        List<FoodServiceProvider> foodServiceProviderList = foodServiceProviderRepository.findAll();
        assertThat(foodServiceProviderList).hasSize(databaseSizeBeforeUpdate);
        FoodServiceProvider testFoodServiceProvider = foodServiceProviderList.get(foodServiceProviderList.size() - 1);
        assertThat(testFoodServiceProvider.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testFoodServiceProvider.getDocProofName()).isEqualTo(UPDATED_DOC_PROOF_NAME);
        assertThat(testFoodServiceProvider.getDocProofNo()).isEqualTo(UPDATED_DOC_PROOF_NO);
        assertThat(testFoodServiceProvider.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testFoodServiceProvider.getContactNo()).isEqualTo(UPDATED_CONTACT_NO);
        assertThat(testFoodServiceProvider.getEmailAddress()).isEqualTo(UPDATED_EMAIL_ADDRESS);
    }

    @Test
    @Transactional
    void patchNonExistingFoodServiceProvider() throws Exception {
        int databaseSizeBeforeUpdate = foodServiceProviderRepository.findAll().size();
        foodServiceProvider.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFoodServiceProviderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, foodServiceProvider.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(foodServiceProvider))
            )
            .andExpect(status().isBadRequest());

        // Validate the FoodServiceProvider in the database
        List<FoodServiceProvider> foodServiceProviderList = foodServiceProviderRepository.findAll();
        assertThat(foodServiceProviderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFoodServiceProvider() throws Exception {
        int databaseSizeBeforeUpdate = foodServiceProviderRepository.findAll().size();
        foodServiceProvider.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFoodServiceProviderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(foodServiceProvider))
            )
            .andExpect(status().isBadRequest());

        // Validate the FoodServiceProvider in the database
        List<FoodServiceProvider> foodServiceProviderList = foodServiceProviderRepository.findAll();
        assertThat(foodServiceProviderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFoodServiceProvider() throws Exception {
        int databaseSizeBeforeUpdate = foodServiceProviderRepository.findAll().size();
        foodServiceProvider.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFoodServiceProviderMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(foodServiceProvider))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FoodServiceProvider in the database
        List<FoodServiceProvider> foodServiceProviderList = foodServiceProviderRepository.findAll();
        assertThat(foodServiceProviderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFoodServiceProvider() throws Exception {
        // Initialize the database
        foodServiceProviderRepository.saveAndFlush(foodServiceProvider);

        int databaseSizeBeforeDelete = foodServiceProviderRepository.findAll().size();

        // Delete the foodServiceProvider
        restFoodServiceProviderMockMvc
            .perform(delete(ENTITY_API_URL_ID, foodServiceProvider.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FoodServiceProvider> foodServiceProviderList = foodServiceProviderRepository.findAll();
        assertThat(foodServiceProviderList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
