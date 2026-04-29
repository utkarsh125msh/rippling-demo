package com.example.rippling_demo.config;


import com.example.rippling_demo.dto.EmployeeRequest;
import com.example.rippling_demo.service.EmployeeService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final EmployeeService employeeService;

    public DataSeeder(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Override
    public void run(String... args) {
        seedEmployee("Alex",    "Rivera",   "alex.rivera@acme.com",   "Engineering", "Software Engineer");
        seedEmployee("Priya",   "Patel",    "priya.patel@acme.com",   "Marketing",   "Product Manager");
        seedEmployee("Marcus",  "Johnson",  "marcus.johnson@acme.com","Finance",     "Analyst");
        seedEmployee("Sophie",  "Chen",     "sophie.chen@acme.com",   "Design",      "Designer");
        seedEmployee("Daniel",  "Kim",      "daniel.kim@acme.com",    "HR",          "Manager");

        System.out.println("\n========================================");
        System.out.println("  Rippling Mini — Demo data loaded ✓");
        System.out.println("  5 employees · 15 app provisions");
        System.out.println("  Frontend → http://localhost:5173");
        System.out.println("  H2 Console → http://localhost:8080/h2-console");
        System.out.println("========================================\n");
    }

    private void seedEmployee(String first, String last, String email, String dept, String role) {
        try {
            EmployeeRequest req = new EmployeeRequest();
            req.setFirstName(first);
            req.setLastName(last);
            req.setEmail(email);
            req.setDepartment(dept);
            req.setRole(role);
            employeeService.createEmployee(req);
            System.out.println("  ✓ Seeded: " + first + " " + last);
        } catch (IllegalArgumentException e) {
            System.out.println("  ⚠ Skipped (already exists): " + email);
        }
    }
}