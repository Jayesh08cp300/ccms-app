package com.pmc.ccms.web.rest;

import com.pmc.ccms.domain.FoodServiceProvider;
import com.pmc.ccms.repository.FoodServiceProviderRepository;
import com.pmc.ccms.service.FoodServiceProviderService;
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
 * REST controller for managing {@link com.pmc.ccms.domain.FoodServiceProvider}.
 */
@RestController
@RequestMapping("/api")
public class FoodServiceProviderResource {

    private final Logger log = LoggerFactory.getLogger(FoodServiceProviderResource.class);

    private static final String ENTITY_NAME = "foodServiceProvider";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FoodServiceProviderService foodServiceProviderService;

    private final FoodServiceProviderRepository foodServiceProviderRepository;

    public FoodServiceProviderResource(
        FoodServiceProviderService foodServiceProviderService,
        FoodServiceProviderRepository foodServiceProviderRepository
    ) {
        this.foodServiceProviderService = foodServiceProviderService;
        this.foodServiceProviderRepository = foodServiceProviderRepository;
    }

    /**
     * {@code POST  /food-service-providers} : Create a new foodServiceProvider.
     *
     * @param foodServiceProvider the foodServiceProvider to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new foodServiceProvider, or with status {@code 400 (Bad Request)} if the foodServiceProvider has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/food-service-providers")
    public ResponseEntity<FoodServiceProvider> createFoodServiceProvider(@RequestBody FoodServiceProvider foodServiceProvider)
        throws URISyntaxException {
        log.debug("REST request to save FoodServiceProvider : {}", foodServiceProvider);
        if (foodServiceProvider.getId() != null) {
            throw new BadRequestAlertException("A new foodServiceProvider cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FoodServiceProvider result = foodServiceProviderService.save(foodServiceProvider);
        return ResponseEntity
            .created(new URI("/api/food-service-providers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /food-service-providers/:id} : Updates an existing foodServiceProvider.
     *
     * @param id the id of the foodServiceProvider to save.
     * @param foodServiceProvider the foodServiceProvider to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated foodServiceProvider,
     * or with status {@code 400 (Bad Request)} if the foodServiceProvider is not valid,
     * or with status {@code 500 (Internal Server Error)} if the foodServiceProvider couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/food-service-providers/{id}")
    public ResponseEntity<FoodServiceProvider> updateFoodServiceProvider(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FoodServiceProvider foodServiceProvider
    ) throws URISyntaxException {
        log.debug("REST request to update FoodServiceProvider : {}, {}", id, foodServiceProvider);
        if (foodServiceProvider.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, foodServiceProvider.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!foodServiceProviderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FoodServiceProvider result = foodServiceProviderService.update(foodServiceProvider);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, foodServiceProvider.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /food-service-providers/:id} : Partial updates given fields of an existing foodServiceProvider, field will ignore if it is null
     *
     * @param id the id of the foodServiceProvider to save.
     * @param foodServiceProvider the foodServiceProvider to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated foodServiceProvider,
     * or with status {@code 400 (Bad Request)} if the foodServiceProvider is not valid,
     * or with status {@code 404 (Not Found)} if the foodServiceProvider is not found,
     * or with status {@code 500 (Internal Server Error)} if the foodServiceProvider couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/food-service-providers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FoodServiceProvider> partialUpdateFoodServiceProvider(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FoodServiceProvider foodServiceProvider
    ) throws URISyntaxException {
        log.debug("REST request to partial update FoodServiceProvider partially : {}, {}", id, foodServiceProvider);
        if (foodServiceProvider.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, foodServiceProvider.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!foodServiceProviderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FoodServiceProvider> result = foodServiceProviderService.partialUpdate(foodServiceProvider);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, foodServiceProvider.getId().toString())
        );
    }

    /**
     * {@code GET  /food-service-providers} : get all the foodServiceProviders.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of foodServiceProviders in body.
     */
    @GetMapping("/food-service-providers")
    public ResponseEntity<List<FoodServiceProvider>> getAllFoodServiceProviders(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of FoodServiceProviders");
        Page<FoodServiceProvider> page = foodServiceProviderService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /food-service-providers/:id} : get the "id" foodServiceProvider.
     *
     * @param id the id of the foodServiceProvider to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the foodServiceProvider, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/food-service-providers/{id}")
    public ResponseEntity<FoodServiceProvider> getFoodServiceProvider(@PathVariable Long id) {
        log.debug("REST request to get FoodServiceProvider : {}", id);
        Optional<FoodServiceProvider> foodServiceProvider = foodServiceProviderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(foodServiceProvider);
    }

    /**
     * {@code DELETE  /food-service-providers/:id} : delete the "id" foodServiceProvider.
     *
     * @param id the id of the foodServiceProvider to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/food-service-providers/{id}")
    public ResponseEntity<Void> deleteFoodServiceProvider(@PathVariable Long id) {
        log.debug("REST request to delete FoodServiceProvider : {}", id);
        foodServiceProviderService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
