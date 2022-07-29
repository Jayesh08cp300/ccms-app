package com.pmc.ccms.service.impl;

import com.pmc.ccms.domain.Menu;
import com.pmc.ccms.repository.MenuRepository;
import com.pmc.ccms.service.MenuService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Menu}.
 */
@Service
@Transactional
public class MenuServiceImpl implements MenuService {

    private final Logger log = LoggerFactory.getLogger(MenuServiceImpl.class);

    private final MenuRepository menuRepository;

    public MenuServiceImpl(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Override
    public Menu save(Menu menu) {
        log.debug("Request to save Menu : {}", menu);
        return menuRepository.save(menu);
    }

    @Override
    public Menu update(Menu menu) {
        log.debug("Request to save Menu : {}", menu);
        return menuRepository.save(menu);
    }

    @Override
    public Optional<Menu> partialUpdate(Menu menu) {
        log.debug("Request to partially update Menu : {}", menu);

        return menuRepository
            .findById(menu.getId())
            .map(existingMenu -> {
                if (menu.getName() != null) {
                    existingMenu.setName(menu.getName());
                }
                if (menu.getServeDate() != null) {
                    existingMenu.setServeDate(menu.getServeDate());
                }
                if (menu.getBookBeforeDate() != null) {
                    existingMenu.setBookBeforeDate(menu.getBookBeforeDate());
                }
                if (menu.getCancelBeforeDate() != null) {
                    existingMenu.setCancelBeforeDate(menu.getCancelBeforeDate());
                }

                return existingMenu;
            })
            .map(menuRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Menu> findAll(Pageable pageable) {
        log.debug("Request to get all Menus");
        return menuRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Menu> findOne(Long id) {
        log.debug("Request to get Menu : {}", id);
        return menuRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Menu : {}", id);
        menuRepository.deleteById(id);
    }
}
