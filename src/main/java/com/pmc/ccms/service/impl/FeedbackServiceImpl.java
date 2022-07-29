package com.pmc.ccms.service.impl;

import com.pmc.ccms.domain.Feedback;
import com.pmc.ccms.repository.FeedbackRepository;
import com.pmc.ccms.service.FeedbackService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Feedback}.
 */
@Service
@Transactional
public class FeedbackServiceImpl implements FeedbackService {

    private final Logger log = LoggerFactory.getLogger(FeedbackServiceImpl.class);

    private final FeedbackRepository feedbackRepository;

    public FeedbackServiceImpl(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    @Override
    public Feedback save(Feedback feedback) {
        log.debug("Request to save Feedback : {}", feedback);
        return feedbackRepository.save(feedback);
    }

    @Override
    public Feedback update(Feedback feedback) {
        log.debug("Request to save Feedback : {}", feedback);
        return feedbackRepository.save(feedback);
    }

    @Override
    public Optional<Feedback> partialUpdate(Feedback feedback) {
        log.debug("Request to partially update Feedback : {}", feedback);

        return feedbackRepository
            .findById(feedback.getId())
            .map(existingFeedback -> {
                if (feedback.getComment() != null) {
                    existingFeedback.setComment(feedback.getComment());
                }
                if (feedback.getDateTime() != null) {
                    existingFeedback.setDateTime(feedback.getDateTime());
                }

                return existingFeedback;
            })
            .map(feedbackRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Feedback> findAll(Pageable pageable) {
        log.debug("Request to get all Feedbacks");
        return feedbackRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Feedback> findOne(Long id) {
        log.debug("Request to get Feedback : {}", id);
        return feedbackRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Feedback : {}", id);
        feedbackRepository.deleteById(id);
    }
}
