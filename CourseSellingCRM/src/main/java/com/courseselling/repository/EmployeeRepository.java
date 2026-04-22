package com.courseselling.repository;

import com.courseselling.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Employee entity.
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Employee findByEmail(String email);
    Employee findByEmployeeCode(String employeeCode);
    List<Employee> findByDepartment(String department);
    List<Employee> findByIsActiveTrue();
}
