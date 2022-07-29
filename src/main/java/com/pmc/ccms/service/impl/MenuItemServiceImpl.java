package com.pmc.ccms.service.impl;

import com.pmc.ccms.domain.MenuItem;
import com.pmc.ccms.repository.MenuItemRepository;
import com.pmc.ccms.service.MenuItemService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link MenuItem}.
 */
@Service
@Transactional
public class MenuItemServiceImpl implements MenuItemService {

    private final Logger log = LoggerFactory.getLogger(MenuItemServiceImpl.class);

    private final MenuItemRepository menuItemRepository;

    public MenuItemServiceImpl(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    @Override
    public MenuItem save(MenuItem menuItem) {
        log.debug("Request to save MenuItem : {}", menuItem);
        return menuItemRepository.save(menuItem);
    }

    @Override
    public MenuItem update(MenuItem menuItem) {
        log.debug("Request to save MenuItem : {}", menuItem);
        return menuItemRepository.save(menuItem);
    }

    @Override
    public Optional<MenuItem> partialUpdate(MenuItem menuItem) {
        log.debug("Request to partially update MenuItem : {}", menuItem);

        return menuItemRepository
            .findById(menuItem.getId())
            .map(existingMenuItem -> {
                if (menuItem.getLimited() != null) {
                    existingMenuItem.setLimited(menuItem.getLimited());
                }

                return existingMenuItem;
            })
            .map(menuItemRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MenuItem> findAll(Pageable pageable) {
        log.debug("Request to get all MenuItems");
        return menuItemRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MenuItem> findOne(Long id) {
        log.debug("Request to get MenuItem : {}", id);
        return menuItemRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete MenuItem : {}", id);
        menuItemRepository.deleteById(id);
    }
}
