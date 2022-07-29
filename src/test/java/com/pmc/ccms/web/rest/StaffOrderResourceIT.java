package com.pmc.ccms.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.pmc.ccms.IntegrationTest;
import com.pmc.ccms.domain.StaffOrder;
import com.pmc.ccms.repository.StaffOrderRepository;
import com.pmc.ccms.service.StaffOrderService;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link StaffOrderResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class StaffOrderResourceIT {

    private static final String ENTITY_API_URL = "/api/staff-orders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StaffOrderRepository staffOrderRepository;

    @Mock
    private StaffOrderRepository staffOrderRepositoryMock;

    @Mock
    private StaffOrderService staffOrderServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStaffOrderMockMvc;

    private StaffOrder staffOrder;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StaffOrder createEntity(EntityManager em) {
        StaffOrder staffOrder = new StaffOrder();
        return staffOrder;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StaffOrder createUpdatedEntity(EntityManager em) {
        StaffOrder staffOrder = new StaffOrder();
        return staffOrder;
    }

    @BeforeEach
    public void initTest() {
        staffOrder = createEntity(em);
    }

    @Test
    @Transactional
    void createStaffOrder() throws Exception {
        int databaseSizeBeforeCreate = staffOrderRepository.findAll().size();
        // Create the StaffOrder
        restStaffOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(staffOrder)))
            .andExpect(status().isCreated());

        // Validate the StaffOrder in the database
        List<StaffOrder> staffOrderList = staffOrderRepository.findAll();
        assertThat(staffOrderList).hasSize(databaseSizeBeforeCreate + 1);
        StaffOrder testStaffOrder = staffOrderList.get(staffOrderList.size() - 1);
    }

    @Test
    @Transactional
    void createStaffOrderWithExistingId() throws Exception {
        // Create the StaffOrder with an existing ID
        staffOrder.setId(1L);

        int databaseSizeBeforeCreate = staffOrderRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStaffOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(staffOrder)))
            .andExpect(status().isBadRequest());

        // Validate the StaffOrder in the database
        List<StaffOrder> staffOrderList = staffOrderRepository.findAll();
        assertThat(staffOrderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllStaffOrders() throws Exception {
        // Initialize the database
        staffOrderRepository.saveAndFlush(staffOrder);

        // Get all the staffOrderList
        restStaffOrderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(staffOrder.getId().intValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllStaffOrdersWithEagerRelationshipsIsEnabled() throws Exception {
        when(staffOrderServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restStaffOrderMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(staffOrderServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllStaffOrdersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(staffOrderServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restStaffOrderMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(staffOrderServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getStaffOrder() throws Exception {
        // Initialize the database
        staffOrderRepository.saveAndFlush(staffOrder);

        // Get the staffOrder
        restStaffOrderMockMvc
            .perform(get(ENTITY_API_URL_ID, staffOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(staffOrder.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingStaffOrder() throws Exception {
        // Get the staffOrder
        restStaffOrderMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewStaffOrder() throws Exception {
        // Initialize the database
        staffOrderRepository.saveAndFlush(staffOrder);

        int databaseSizeBeforeUpdate = staffOrderRepository.findAll().size();

        // Update the staffOrder
        StaffOrder updatedStaffOrder = staffOrderRepository.findById(staffOrder.getId()).get();
        // Disconnect from session so that the updates on updatedStaffOrder are not directly saved in db
        em.detach(updatedStaffOrder);

        restStaffOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedStaffOrder.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedStaffOrder))
            )
            .andExpect(status().isOk());

        // Validate the StaffOrder in the database
        List<StaffOrder> staffOrderList = staffOrderRepository.findAll();
        assertThat(staffOrderList).hasSize(databaseSizeBeforeUpdate);
        StaffOrder testStaffOrder = staffOrderList.get(staffOrderList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingStaffOrder() throws Exception {
        int databaseSizeBeforeUpdate = staffOrderRepository.findAll().size();
        staffOrder.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStaffOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, staffOrder.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(staffOrder))
            )
            .andExpect(status().isBadRequest());

        // Validate the StaffOrder in the database
        List<StaffOrder> staffOrderList = staffOrderRepository.findAll();
        assertThat(staffOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStaffOrder() throws Exception {
        int databaseSizeBeforeUpdate = staffOrderRepository.findAll().size();
        staffOrder.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStaffOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(staffOrder))
            )
            .andExpect(status().isBadRequest());

        // Validate the StaffOrder in the database
        List<StaffOrder> staffOrderList = staffOrderRepository.findAll();
        assertThat(staffOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStaffOrder() throws Exception {
        int databaseSizeBeforeUpdate = staffOrderRepository.findAll().size();
        staffOrder.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStaffOrderMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(staffOrder)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the StaffOrder in the database
        List<StaffOrder> staffOrderList = staffOrderRepository.findAll();
        assertThat(staffOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStaffOrderWithPatch() throws Exception {
        // Initialize the database
        staffOrderRepository.saveAndFlush(staffOrder);

        int databaseSizeBeforeUpdate = staffOrderRepository.findAll().size();

        // Update the staffOrder using partial update
        StaffOrder partialUpdatedStaffOrder = new StaffOrder();
        partialUpdatedStaffOrder.setId(staffOrder.getId());

        restStaffOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStaffOrder.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStaffOrder))
            )
            .andExpect(status().isOk());

        // Validate the StaffOrder in the database
        List<StaffOrder> staffOrderList = staffOrderRepository.findAll();
        assertThat(staffOrderList).hasSize(databaseSizeBeforeUpdate);
        StaffOrder testStaffOrder = staffOrderList.get(staffOrderList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateStaffOrderWithPatch() throws Exception {
        // Initialize the database
        staffOrderRepository.saveAndFlush(staffOrder);

        int databaseSizeBeforeUpdate = staffOrderRepository.findAll().size();

        // Update the staffOrder using partial update
        StaffOrder partialUpdatedStaffOrder = new StaffOrder();
        partialUpdatedStaffOrder.setId(staffOrder.getId());

        restStaffOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStaffOrder.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStaffOrder))
            )
            .andExpect(status().isOk());

        // Validate the StaffOrder in the database
        List<StaffOrder> staffOrderList = staffOrderRepository.findAll();
        assertThat(staffOrderList).hasSize(databaseSizeBeforeUpdate);
        StaffOrder testStaffOrder = staffOrderList.get(staffOrderList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingStaffOrder() throws Exception {
        int databaseSizeBeforeUpdate = staffOrderRepository.findAll().size();
        staffOrder.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStaffOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, staffOrder.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(staffOrder))
            )
            .andExpect(status().isBadRequest());

        // Validate the StaffOrder in the database
        List<StaffOrder> staffOrderList = staffOrderRepository.findAll();
        assertThat(staffOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStaffOrder() throws Exception {
        int databaseSizeBeforeUpdate = staffOrderRepository.findAll().size();
        staffOrder.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStaffOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(staffOrder))
            )
            .andExpect(status().isBadRequest());

        // Validate the StaffOrder in the database
        List<StaffOrder> staffOrderList = staffOrderRepository.findAll();
        assertThat(staffOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStaffOrder() throws Exception {
        int databaseSizeBeforeUpdate = staffOrderRepository.findAll().size();
        staffOrder.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStaffOrderMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(staffOrder))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StaffOrder in the database
        List<StaffOrder> staffOrderList = staffOrderRepository.findAll();
        assertThat(staffOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStaffOrder() throws Exception {
        // Initialize the database
        staffOrderRepository.saveAndFlush(staffOrder);

        int databaseSizeBeforeDelete = staffOrderRepository.findAll().size();

        // Delete the staffOrder
        restStaffOrderMockMvc
            .perform(delete(ENTITY_API_URL_ID, staffOrder.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<StaffOrder> staffOrderList = staffOrderRepository.findAll();
        assertThat(staffOrderList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
