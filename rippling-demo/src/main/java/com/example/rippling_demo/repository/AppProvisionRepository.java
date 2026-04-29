package com.example.rippling_demo.repository;

import com.example.rippling_demo.entity.AppProvision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppProvisionRepository extends JpaRepository<AppProvision, Long> {
    List<AppProvision> findByEmployeeId(Long employeeId);
}