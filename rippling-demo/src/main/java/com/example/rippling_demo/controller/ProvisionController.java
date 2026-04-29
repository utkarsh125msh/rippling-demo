package com.example.rippling_demo.controller;


import com.example.rippling_demo.dto.EmployeeResponse.AppProvisionDto;
import com.example.rippling_demo.dto.WorkflowLogDto;
import com.example.rippling_demo.service.ProvisionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/provisions")
public class ProvisionController {

    private final ProvisionService provisionService;

    public ProvisionController(ProvisionService provisionService) {
        this.provisionService = provisionService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProvision(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(provisionService.getProvision(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<?> completeProvision(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(provisionService.completeProvision(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<AppProvisionDto>> getByEmployee(@PathVariable Long employeeId) {
        return ResponseEntity.ok(provisionService.getByEmployee(employeeId));
    }

    @GetMapping("/logs/employee/{employeeId}")
    public ResponseEntity<List<WorkflowLogDto>> getLogs(@PathVariable Long employeeId) {
        return ResponseEntity.ok(provisionService.getLogsByEmployee(employeeId));
    }
}