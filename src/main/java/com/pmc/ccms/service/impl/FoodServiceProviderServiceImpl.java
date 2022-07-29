package com.pmc.ccms.service.impl;

import com.pmc.ccms.domain.FoodServiceProvider;
import com.pmc.ccms.repository.FoodServiceProviderRepository;
import com.pmc.ccms.service.FoodServiceProviderService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FoodServiceProvider}.
 */
@Service
@Transactional
public class FoodServiceProviderServiceImpl implements FoodServiceProviderService {

    private final Logger log = LoggerFactory.getLogger(FoodServiceProviderServiceImpl.class);

    private final FoodServiceProviderRepository foodServiceProviderRepository;

    public FoodServiceProviderServiceImpl(FoodServiceProviderRepository foodServiceProviderRepository) {
        this.foodServiceProviderRepository = foodServiceProviderRepository;
    }

    @Override
    public FoodServiceProvider save(FoodServiceProvider foodServiceProvider) {
        log.debug("Request to save FoodServiceProvider : {}", foodServiceProvider);
        return foodServiceProviderRepository.save(foodServiceProvider);
    }

    @Override
    public FoodServiceProvider update(FoodServiceProvider foodServiceProvider) {
        log.debug("Request to save FoodServiceProvider : {}", foodServiceProvider);
        return foodServiceProviderRepository.save(foodServiceProvider);
    }

    @Override
    public Optional<FoodServiceProvider> partialUpdate(FoodServiceProvider foodServiceProvider) {
        log.debug("Request to partially update FoodServiceProvider : {}", foodServiceProvider);

        return foodServiceProviderRepository
            .findById(foodServiceProvider.getId())
            .map(existingFoodServiceProvider -> {
                if (foodServiceProvider.getFullName() != null) {
                    existingFoodServiceProvider.setFullName(foodServiceProvider.getFullName());
                }
                if (foodServiceProvider.getDocProofName() != null) {
                    existingFoodServiceProvider.setDocProofName(foodServiceProvider.getDocProofName());
                }
                if (foodServiceProvider.getDocProofNo() != null) {
                    existingFoodServiceProvider.setDocProofNo(foodServiceProvider.getDocProofNo());
                }
                if (foodServiceProvider.getAddress() != null) {
                    existingFoodServiceProvider.setAddress(foodServiceProvider.getAddress());
                }
                if (foodServiceProvider.getContactNo() != null) {
                    existingFoodServiceProvider.setContactNo(foodServiceProvider.getContactNo());
                }
                if (foodServiceProvider.getEmailAddress() != null) {
                    existingFoodServiceProvider.setEmailAddress(foodServiceProvider.getEmailAddress());
                }

                return existingFoodServiceProvider;
            })
            .map(foodServiceProviderRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FoodServiceProvider> findAll(Pageable pageable) {
        log.debug("Request to get all FoodServiceProviders");
        return foodServiceProviderRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FoodServiceProvider> findOne(Long id) {
        log.debug("Request to get FoodServiceProvider : {}", id);
        return foodServiceProviderRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FoodServiceProvider : {}", id);
        foodServiceProviderRepository.deleteById(id);
    }
}
