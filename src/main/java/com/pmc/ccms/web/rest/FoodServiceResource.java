package com.pmc.ccms.web.rest;

import com.pmc.ccms.domain.FoodService;
import com.pmc.ccms.repository.FoodServiceRepository;
import com.pmc.ccms.service.FoodServiceService;
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
 * REST controller for managing {@link com.pmc.ccms.domain.FoodService}.
 */
@RestController
@RequestMapping("/api")
public class FoodServiceResource {

    private final Logger log = LoggerFactory.getLogger(FoodServiceResource.class);

    private static final String ENTITY_NAME = "foodService";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FoodServiceService foodServiceService;

    private final FoodServiceRepository foodServiceRepository;

    public FoodServiceResource(FoodServiceService foodServiceService, FoodServiceRepository foodServiceRepository) {
        this.foodServiceService = foodServiceService;
        this.foodServiceRepository = foodServiceRepository;
    }

    /**
     * {@code POST  /food-services} : Create a new foodService.
     *
     * @param foodService the foodService to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new foodService, or with status {@code 400 (Bad Request)} if the foodService has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/food-services")
    public ResponseEntity<FoodService> createFoodService(@RequestBody FoodService foodService) throws URISyntaxException {
        log.debug("REST request to save FoodService : {}", foodService);
        if (foodService.getId() != null) {
            throw new BadRequestAlertException("A new foodService cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FoodService result = foodServiceService.save(foodService);
        return ResponseEntity
            .created(new URI("/api/food-services/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /food-services/:id} : Updates an existing foodService.
     *
     * @param id the id of the foodService to save.
     * @param foodService the foodService to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated foodService,
     * or with status {@code 400 (Bad Request)} if the foodService is not valid,
     * or with status {@code 500 (Internal Server Error)} if the foodService couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/food-services/{id}")
    public ResponseEntity<FoodService> updateFoodService(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FoodService foodService
    ) throws URISyntaxException {
        log.debug("REST request to update FoodService : {}, {}", id, foodService);
        if (foodService.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, foodService.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!foodServiceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FoodService result = foodServiceService.update(foodService);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, foodService.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /food-services/:id} : Partial updates given fields of an existing foodService, field will ignore if it is null
     *
     * @param id the id of the foodService to save.
     * @param foodService the foodService to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated foodService,
     * or with status {@code 400 (Bad Request)} if the foodService is not valid,
     * or with status {@code 404 (Not Found)} if the foodService is not found,
     * or with status {@code 500 (Internal Server Error)} if the foodService couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/food-services/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FoodService> partialUpdateFoodService(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FoodService foodService
    ) throws URISyntaxException {
        log.debug("REST request to partial update FoodService partially : {}, {}", id, foodService);
        if (foodService.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, foodService.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!foodServiceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FoodService> result = foodServiceService.partialUpdate(foodService);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, foodService.getId().toString())
        );
    }

    /**
     * {@code GET  /food-services} : get all the foodServices.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of foodServices in body.
     */
    @GetMapping("/food-services")
    public ResponseEntity<List<FoodService>> getAllFoodServices(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of FoodServices");
        Page<FoodService> page = foodServiceService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /food-services/:id} : get the "id" foodService.
     *
     * @param id the id of the foodService to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the foodService, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/food-services/{id}")
    public ResponseEntity<FoodService> getFoodService(@PathVariable Long id) {
        log.debug("REST request to get FoodService : {}", id);
        Optional<FoodService> foodService = foodServiceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(foodService);
    }

    /**
     * {@code DELETE  /food-services/:id} : delete the "id" foodService.
     *
     * @param id the id of the foodService to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/food-services/{id}")
    public ResponseEntity<Void> deleteFoodService(@PathVariable Long id) {
        log.debug("REST request to delete FoodService : {}", id);
        foodServiceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
