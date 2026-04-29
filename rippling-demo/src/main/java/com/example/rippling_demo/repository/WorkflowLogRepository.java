package com.example.rippling_demo.repository;

import com.example.rippling_demo.entity.WorkFlowLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkflowLogRepository extends JpaRepository<WorkFlowLog, Long> {
    List<WorkFlowLog> findByEmployeeIdOrderByTimestampDesc(Long employeeId);
}