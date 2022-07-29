package com.pmc.ccms.web.rest;

import com.pmc.ccms.domain.StaffOrder;
import com.pmc.ccms.repository.StaffOrderRepository;
import com.pmc.ccms.service.StaffOrderService;
import com.pmc.ccms.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.pmc.ccms.domain.StaffOrder}.
 */
@RestController
@RequestMapping("/api")
public class StaffOrderResource {

    private final Logger log = LoggerFactory.getLogger(StaffOrderResource.class);

    private static final String ENTITY_NAME = "staffOrder";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StaffOrderService staffOrderService;

    private final StaffOrderRepository staffOrderRepository;

    public StaffOrderResource(StaffOrderService staffOrderService, StaffOrderRepository staffOrderRepository) {
        this.staffOrderService = staffOrderService;
        this.staffOrderRepository = staffOrderRepository;
    }

    /**
     * {@code POST  /staff-orders} : Create a new staffOrder.
     *
     * @param staffOrder the staffOrder to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new staffOrder, or with status {@code 400 (Bad Request)} if the staffOrder has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/staff-orders")
    public ResponseEntity<StaffOrder> createStaffOrder(@RequestBody StaffOrder staffOrder) throws URISyntaxException {
        log.debug("REST request to save StaffOrder : {}", staffOrder);
        if (staffOrder.getId() != null) {
            throw new BadRequestAlertException("A new staffOrder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StaffOrder result = staffOrderService.save(staffOrder);
        return ResponseEntity
            .created(new URI("/api/staff-orders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /staff-orders/:id} : Updates an existing staffOrder.
     *
     * @param id the id of the staffOrder to save.
     * @param staffOrder the staffOrder to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated staffOrder,
     * or with status {@code 400 (Bad Request)} if the staffOrder is not valid,
     * or with status {@code 500 (Internal Server Error)} if the staffOrder couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/staff-orders/{id}")
    public ResponseEntity<StaffOrder> updateStaffOrder(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody StaffOrder staffOrder
    ) throws URISyntaxException {
        log.debug("REST request to update StaffOrder : {}, {}", id, staffOrder);
        if (staffOrder.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, staffOrder.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!staffOrderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        StaffOrder result = staffOrderService.update(staffOrder);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, staffOrder.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /staff-orders/:id} : Partial updates given fields of an existing staffOrder, field will ignore if it is null
     *
     * @param id the id of the staffOrder to save.
     * @param staffOrder the staffOrder to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated staffOrder,
     * or with status {@code 400 (Bad Request)} if the staffOrder is not valid,
     * or with status {@code 404 (Not Found)} if the staffOrder is not found,
     * or with status {@code 500 (Internal Server Error)} if the staffOrder couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/staff-orders/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StaffOrder> partialUpdateStaffOrder(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody StaffOrder staffOrder
    ) throws URISyntaxException {
        log.debug("REST request to partial update StaffOrder partially : {}, {}", id, staffOrder);
        if (staffOrder.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, staffOrder.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!staffOrderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StaffOrder> result = staffOrderService.partialUpdate(staffOrder);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, staffOrder.getId().toString())
        );
    }

    /**
     * {@code GET  /staff-orders} : get all the staffOrders.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of staffOrders in body.
     */
    @GetMapping("/staff-orders")
    public ResponseEntity<List<StaffOrder>> getAllStaffOrders(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of StaffOrders");
        Page<StaffOrder> page;
        if (eagerload) {
            page = staffOrderService.findAllWithEagerRelationships(pageable);
        } else {
            page = staffOrderService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /staff-orders/:id} : get the "id" staffOrder.
     *
     * @param id the id of the staffOrder to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the staffOrder, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/staff-orders/{id}")
    public ResponseEntity<StaffOrder> getStaffOrder(@PathVariable Long id) {
        log.debug("REST request to get StaffOrder : {}", id);
        Optional<StaffOrder> staffOrder = staffOrderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(staffOrder);
    }

    /**
     * {@code DELETE  /staff-orders/:id} : delete the "id" staffOrder.
     *
     * @param id the id of the staffOrder to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/staff-orders/{id}")
    public ResponseEntity<Void> deleteStaffOrder(@PathVariable Long id) {
        log.debug("REST request to delete StaffOrder : {}", id);
        staffOrderService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
