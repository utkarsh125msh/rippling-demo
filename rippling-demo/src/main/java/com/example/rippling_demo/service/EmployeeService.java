package com.example.rippling_demo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.rippling_demo.dto.EmployeeRequest;
import com.example.rippling_demo.dto.EmployeeResponse;
import com.example.rippling_demo.entity.AppProvision;
import com.example.rippling_demo.entity.Employee;
import com.example.rippling_demo.entity.WorkFlowLog;
import com.example.rippling_demo.repository.AppProvisionRepository;
import com.example.rippling_demo.repository.EmployeeRepository;
import com.example.rippling_demo.repository.WorkflowLogRepository;


import java.util.List;
import java.util.stream.Collectors;
 
@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepo;
    private final AppProvisionRepository provisionRepo;
    private final WorkflowLogRepository logRepo;

    public EmployeeService(EmployeeRepository employeeRepo, AppProvisionRepository provisionRepo, WorkflowLogRepository logRepo) {
        this.employeeRepo = employeeRepo;
        this.provisionRepo = provisionRepo;
        this.logRepo = logRepo;
    }

    @Transactional
    public EmployeeResponse createEmployee(EmployeeRequest request){
        
      // 1. Validate email uniqueness
        if (employeeRepo.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + request.getEmail());
        }
           

         // 2. Create and save employee
        Employee employee = new Employee();
        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setEmail(request.getEmail());
        employee.setDepartment(request.getDepartment());
        employee.setRole(request.getRole());
        employee.setStatus("ACTIVE");
        Employee savedEmployee = employeeRepo.save(employee);


        //3. log employ creation
        log("Employee created: ","Employee " + savedEmployee.getFirstName() + " " + savedEmployee.getLastName() + " Created", savedEmployee.getId(), null);
    
        // 4. Auto-provision 3 apps
        List<AppProvision> provisions = List.of(
            buildProvision(savedEmployee, "Gmail",   "EMAIL",     generateEmail(savedEmployee)),
            buildProvision(savedEmployee, "Slack",   "MESSAGING", generateSlackHandle(savedEmployee)),
            buildProvision(savedEmployee, "Payroll", "FINANCE",   generatePayrollId(savedEmployee))
        );

        List<AppProvision> saved = provisionRepo.saveAll(provisions);

        // 5. Log each provision
        saved.forEach(p -> log("APP_PROVISIONED",
            p.getAppName() + " provisioned for " + savedEmployee.getEmail(),
            savedEmployee.getId(), p.getId()));

        // 6. Build and return response
        return toResponse(savedEmployee, saved);
      
    }
//------------------helper function--------------------
    private AppProvision buildProvision(Employee employee, String appName, String appType, String account) {
        AppProvision p = new AppProvision();
        p.setEmployee(employee);
        p.setAppName(appName);
        p.setAppType(appType);
        p.setStatus("PROVISIONED");
        p.setProvisionedAccount(account);
        return p;
    }


    private String generateEmail(Employee e) {
        return (e.getFirstName().toLowerCase() + "." + e.getLastName().toLowerCase() + "@company.com");
    }

    private String generateSlackHandle(Employee e) {
        return "@" + e.getFirstName().toLowerCase() + e.getLastName().toLowerCase();
    }

    private String generatePayrollId(Employee e) {
        return "PAY-" + e.getId() + "-" + e.getDepartment().toUpperCase().replaceAll("\\s+", "");
    }

    private void log(String eventType, String message, Long employeeId, Long provisionId) {
        WorkFlowLog wl = new WorkFlowLog();
        wl.setEventType(eventType);
        wl.setMessage(message);
        wl.setEmployeeId(employeeId);
        wl.setProvisionId(provisionId);
        logRepo.save(wl);
    }

    private EmployeeResponse toResponse(Employee emp, List<AppProvision> provisions) {
        List<EmployeeResponse.AppProvisionDto> dtos = provisions.stream()
            .map(p -> new EmployeeResponse.AppProvisionDto(
                p.getId(),
                p.getAppName(),
                p.getAppType(),
                p.getStatus(),
                p.getProvisionedAccount(),
                p.getCreatedAt(),
                p.getCompletedAt()
            ))
            .collect(Collectors.toList());

        return new EmployeeResponse(
            emp.getId(),
            emp.getFirstName(),
            emp.getLastName(),
            emp.getEmail(),
            emp.getDepartment(),
            emp.getRole(),
            emp.getStatus(),
            emp.getCreatedAt(),
            dtos
        );
    }
    
    public List<EmployeeResponse> getAllEmployees() {
        return employeeRepo.findAll().stream()
            .map(emp -> {
                List<AppProvision> provisions = provisionRepo.findByEmployeeId(emp.getId());
                return toResponse(emp, provisions);
            })
            .collect(Collectors.toList());
    }

    public EmployeeResponse getEmployee(Long id) {
        Employee emp = employeeRepo.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Employee not found: " + id));
        List<AppProvision> provisions = provisionRepo.findByEmployeeId(emp.getId());
        return toResponse(emp, provisions);
    }
}

