package com.example.rippling_demo.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "workflow_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class WorkFlowLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String eventType;  // e.g. "EMPLOYEE_CREATED", "APP_PROVISIONED", "PROVISION_COMPLETED"

    @Column(nullable = false)
    private String message;

    @Column 
    private Long employeeId;  

    @Column
    private Long provisionId;

    @Column(nullable = false)
    private LocalDateTime timestamp = LocalDateTime.now(); 


}
