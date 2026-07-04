package com.ems.controller;

import com.ems.model.Employee;
import com.ems.service.EmployeeService;
import com.ems.service.PdfExportService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/employees")
@CrossOrigin(origins = "*")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final PdfExportService pdfExportService;

    public EmployeeController(EmployeeService employeeService, PdfExportService pdfExportService) {
        this.employeeService = employeeService;
        this.pdfExportService = pdfExportService;
    }

    @PostMapping
    public ResponseEntity<Employee> createEmployee(@Valid @RequestBody Employee employee) {
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeService.createEmployee(employee));
    }

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String department) {
        List<Employee> employees;
        if (search != null && !search.isBlank()) {
            employees = employeeService.searchEmployees(search);
        } else if (department != null && !department.isBlank()) {
            employees = employeeService.getEmployeesByDepartment(department);
        } else {
            employees = employeeService.getAllEmployees();
        }
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @Valid @RequestBody Employee employee) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, employee));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok(Map.of("message", "Employee deleted successfully"));
    }

    @GetMapping("/departments")
    public ResponseEntity<List<String>> getDepartments() {
        return ResponseEntity.ok(employeeService.getAllDepartments());
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        return ResponseEntity.ok(employeeService.getDashboardStats());
    }

    @GetMapping("/export/pdf")
    public ResponseEntity<byte[]> exportToPdf() throws Exception {
        List<Employee> employees = employeeService.getAllEmployees();
        byte[] pdf = pdfExportService.exportEmployeesToPdf(employees);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "employees_report.pdf");
        return ResponseEntity.ok().headers(headers).body(pdf);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
    }
}