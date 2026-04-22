package com.courseselling.repository;

import com.courseselling.model.CustomerInteraction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for CustomerInteraction entity.
 */
@Repository
public interface CustomerInteractionRepository extends JpaRepository<CustomerInteraction, Long> {
    List<CustomerInteraction> findByUserId(Long userId);
    List<CustomerInteraction> findByEmployeeId(Long employeeId);
    List<CustomerInteraction> findByUserIdOrderByInteractionDateDesc(Long userId);
}
