package com.example.rippling_demo.service;

import com.example.rippling_demo.dto.WorkflowLogDto;
import com.example.rippling_demo.dto.EmployeeResponse.AppProvisionDto;
import com.example.rippling_demo.entity.AppProvision;
import com.example.rippling_demo.entity.WorkFlowLog;
import com.example.rippling_demo.repository.AppProvisionRepository;
import com.example.rippling_demo.repository.WorkflowLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProvisionService {

    private final AppProvisionRepository provisionRepo;
    private final WorkflowLogRepository logRepo;

    public ProvisionService(AppProvisionRepository provisionRepo,
                            WorkflowLogRepository logRepo) {
        this.provisionRepo = provisionRepo;
        this.logRepo = logRepo;
    }

    public AppProvisionDto getProvision(Long id) {
        AppProvision p = provisionRepo.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Provision not found: " + id));
        return toDto(p);
    }

    public List<AppProvisionDto> getByEmployee(Long employeeId) {
        return provisionRepo.findByEmployeeId(employeeId)
            .stream().map(this::toDto).collect(Collectors.toList());
    }

    @Transactional
    public AppProvisionDto completeProvision(Long id) {
        AppProvision p = provisionRepo.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Provision not found: " + id));

        if ("DONE".equals(p.getStatus())) {
            throw new IllegalArgumentException("Provision already completed: " + id);
        }

        p.setStatus("DONE");
        p.setCompletedAt(LocalDateTime.now());
        provisionRepo.save(p);

        // Log the completion
        WorkFlowLog wl = new WorkFlowLog();
        wl.setEventType("PROVISION_COMPLETED");
        wl.setMessage(p.getAppName() + " provisioning completed for account: " + p.getProvisionedAccount());
        wl.setEmployeeId(p.getEmployee().getId());
        wl.setProvisionId(p.getId());
        logRepo.save(wl);

        return toDto(p);
    }

    public List<WorkflowLogDto> getLogsByEmployee(Long employeeId) {
        return logRepo.findByEmployeeIdOrderByTimestampDesc(employeeId)
            .stream()
            .map(l -> new WorkflowLogDto(
                l.getId(),
                l.getEventType(),
                l.getMessage(),
                l.getEmployeeId(),
                l.getProvisionId(),
                l.getTimestamp()
            ))
            .collect(Collectors.toList());
    }

    private AppProvisionDto toDto(AppProvision p) {
        return new AppProvisionDto(
            p.getId(),
            p.getAppName(),
            p.getAppType(),
            p.getStatus(),
            p.getProvisionedAccount(),
            p.getCreatedAt(),
            p.getCompletedAt()
        );
    }
}
