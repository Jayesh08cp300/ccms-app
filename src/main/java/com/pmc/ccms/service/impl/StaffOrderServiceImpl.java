package com.pmc.ccms.service.impl;

import com.pmc.ccms.domain.StaffOrder;
import com.pmc.ccms.repository.StaffOrderRepository;
import com.pmc.ccms.service.StaffOrderService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link StaffOrder}.
 */
@Service
@Transactional
public class StaffOrderServiceImpl implements StaffOrderService {

    private final Logger log = LoggerFactory.getLogger(StaffOrderServiceImpl.class);

    private final StaffOrderRepository staffOrderRepository;

    public StaffOrderServiceImpl(StaffOrderRepository staffOrderRepository) {
        this.staffOrderRepository = staffOrderRepository;
    }

    @Override
    public StaffOrder save(StaffOrder staffOrder) {
        log.debug("Request to save StaffOrder : {}", staffOrder);
        return staffOrderRepository.save(staffOrder);
    }

    @Override
    public StaffOrder update(StaffOrder staffOrder) {
        log.debug("Request to save StaffOrder : {}", staffOrder);
        return staffOrderRepository.save(staffOrder);
    }

    @Override
    public Optional<StaffOrder> partialUpdate(StaffOrder staffOrder) {
        log.debug("Request to partially update StaffOrder : {}", staffOrder);

        return staffOrderRepository
            .findById(staffOrder.getId())
            .map(existingStaffOrder -> {
                return existingStaffOrder;
            })
            .map(staffOrderRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StaffOrder> findAll(Pageable pageable) {
        log.debug("Request to get all StaffOrders");
        return staffOrderRepository.findAll(pageable);
    }

    public Page<StaffOrder> findAllWithEagerRelationships(Pageable pageable) {
        return staffOrderRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<StaffOrder> findOne(Long id) {
        log.debug("Request to get StaffOrder : {}", id);
        return staffOrderRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete StaffOrder : {}", id);
        staffOrderRepository.deleteById(id);
    }
}
