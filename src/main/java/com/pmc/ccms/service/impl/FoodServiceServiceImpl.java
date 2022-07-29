package com.pmc.ccms.service.impl;

import com.pmc.ccms.domain.FoodService;
import com.pmc.ccms.repository.FoodServiceRepository;
import com.pmc.ccms.service.FoodServiceService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FoodService}.
 */
@Service
@Transactional
public class FoodServiceServiceImpl implements FoodServiceService {

    private final Logger log = LoggerFactory.getLogger(FoodServiceServiceImpl.class);

    private final FoodServiceRepository foodServiceRepository;

    public FoodServiceServiceImpl(FoodServiceRepository foodServiceRepository) {
        this.foodServiceRepository = foodServiceRepository;
    }

    @Override
    public FoodService save(FoodService foodService) {
        log.debug("Request to save FoodService : {}", foodService);
        return foodServiceRepository.save(foodService);
    }

    @Override
    public FoodService update(FoodService foodService) {
        log.debug("Request to save FoodService : {}", foodService);
        return foodServiceRepository.save(foodService);
    }

    @Override
    public Optional<FoodService> partialUpdate(FoodService foodService) {
        log.debug("Request to partially update FoodService : {}", foodService);

        return foodServiceRepository
            .findById(foodService.getId())
            .map(existingFoodService -> {
                if (foodService.getName() != null) {
                    existingFoodService.setName(foodService.getName());
                }
                if (foodService.getRate() != null) {
                    existingFoodService.setRate(foodService.getRate());
                }
                if (foodService.getStartDate() != null) {
                    existingFoodService.setStartDate(foodService.getStartDate());
                }
                if (foodService.getEndDate() != null) {
                    existingFoodService.setEndDate(foodService.getEndDate());
                }

                return existingFoodService;
            })
            .map(foodServiceRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FoodService> findAll(Pageable pageable) {
        log.debug("Request to get all FoodServices");
        return foodServiceRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FoodService> findOne(Long id) {
        log.debug("Request to get FoodService : {}", id);
        return foodServiceRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FoodService : {}", id);
        foodServiceRepository.deleteById(id);
    }
}
