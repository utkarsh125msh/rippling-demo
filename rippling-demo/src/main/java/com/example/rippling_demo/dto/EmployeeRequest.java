package com.example.rippling_demo.dto;


import lombok.Data;

@Data
public class EmployeeRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String department;
    private String role;
}