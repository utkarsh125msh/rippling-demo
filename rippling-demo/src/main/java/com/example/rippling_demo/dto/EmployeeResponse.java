package com.example.rippling_demo.dto;

import lombok.Data;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class EmployeeResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String department;
    private String role;
    private String status;
    private LocalDateTime createdAt;
    private List<AppProvisionDto> appProvisions;

    @Data
    @AllArgsConstructor
    public static class AppProvisionDto {
        private Long id;
        private String appName;
        private String appType;
        private String status;
        private String provisionedAccount;
        private LocalDateTime createdAt;
        private LocalDateTime completedAt;
    }
}