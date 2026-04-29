package com.example.rippling_demo.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "app_provisions")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class AppProvision {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emplloyee_id", nullable = false )
    private Employee employee;

    @Column(nullable = false)
    private String appName;   // e.g. "Gmail", "Slack", "Payroll"

    @Column(nullable = false)
    private String appType;   // e.g. "EMAIL", "MESSAGING", "FINANCE"

    @Column(nullable = false)
    private String status = "PENDING";

    @Column
    private String provisionedAccount;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column
    private LocalDateTime completedAt;
    
}
