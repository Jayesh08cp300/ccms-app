package com.pmc.ccms.web.rest;

import com.pmc.ccms.domain.MenuItem;
import com.pmc.ccms.repository.MenuItemRepository;
import com.pmc.ccms.service.MenuItemService;
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
 * REST controller for managing {@link com.pmc.ccms.domain.MenuItem}.
 */
@RestController
@RequestMapping("/api")
public class MenuItemResource {

    private final Logger log = LoggerFactory.getLogger(MenuItemResource.class);

    private static final String ENTITY_NAME = "menuItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MenuItemService menuItemService;

    private final MenuItemRepository menuItemRepository;

    public MenuItemResource(MenuItemService menuItemService, MenuItemRepository menuItemRepository) {
        this.menuItemService = menuItemService;
        this.menuItemRepository = menuItemRepository;
    }

    /**
     * {@code POST  /menu-items} : Create a new menuItem.
     *
     * @param menuItem the menuItem to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new menuItem, or with status {@code 400 (Bad Request)} if the menuItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/menu-items")
    public ResponseEntity<MenuItem> createMenuItem(@RequestBody MenuItem menuItem) throws URISyntaxException {
        log.debug("REST request to save MenuItem : {}", menuItem);
        if (menuItem.getId() != null) {
            throw new BadRequestAlertException("A new menuItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MenuItem result = menuItemService.save(menuItem);
        return ResponseEntity
            .created(new URI("/api/menu-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /menu-items/:id} : Updates an existing menuItem.
     *
     * @param id the id of the menuItem to save.
     * @param menuItem the menuItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated menuItem,
     * or with status {@code 400 (Bad Request)} if the menuItem is not valid,
     * or with status {@code 500 (Internal Server Error)} if the menuItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/menu-items/{id}")
    public ResponseEntity<MenuItem> updateMenuItem(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MenuItem menuItem
    ) throws URISyntaxException {
        log.debug("REST request to update MenuItem : {}, {}", id, menuItem);
        if (menuItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, menuItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!menuItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MenuItem result = menuItemService.update(menuItem);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, menuItem.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /menu-items/:id} : Partial updates given fields of an existing menuItem, field will ignore if it is null
     *
     * @param id the id of the menuItem to save.
     * @param menuItem the menuItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated menuItem,
     * or with status {@code 400 (Bad Request)} if the menuItem is not valid,
     * or with status {@code 404 (Not Found)} if the menuItem is not found,
     * or with status {@code 500 (Internal Server Error)} if the menuItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/menu-items/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MenuItem> partialUpdateMenuItem(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MenuItem menuItem
    ) throws URISyntaxException {
        log.debug("REST request to partial update MenuItem partially : {}, {}", id, menuItem);
        if (menuItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, menuItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!menuItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MenuItem> result = menuItemService.partialUpdate(menuItem);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, menuItem.getId().toString())
        );
    }

    /**
     * {@code GET  /menu-items} : get all the menuItems.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of menuItems in body.
     */
    @GetMapping("/menu-items")
    public ResponseEntity<List<MenuItem>> getAllMenuItems(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of MenuItems");
        Page<MenuItem> page = menuItemService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /menu-items/:id} : get the "id" menuItem.
     *
     * @param id the id of the menuItem to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the menuItem, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/menu-items/{id}")
    public ResponseEntity<MenuItem> getMenuItem(@PathVariable Long id) {
        log.debug("REST request to get MenuItem : {}", id);
        Optional<MenuItem> menuItem = menuItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(menuItem);
    }

    /**
     * {@code DELETE  /menu-items/:id} : delete the "id" menuItem.
     *
     * @param id the id of the menuItem to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/menu-items/{id}")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable Long id) {
        log.debug("REST request to delete MenuItem : {}", id);
        menuItemService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
