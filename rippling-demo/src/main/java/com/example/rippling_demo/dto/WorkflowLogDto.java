package com.example.rippling_demo.dto;

import lombok.Data;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class WorkflowLogDto {
    private Long id;
    private String eventType;
    private String message;
    private Long employeeId;
    private Long provisionId;
    private LocalDateTime timestamp;
}