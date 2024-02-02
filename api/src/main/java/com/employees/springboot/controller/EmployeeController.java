package com.employees.springboot.controller;

import com.employees.springboot.exception.ResourceNotFoundException;
import com.employees.springboot.model.Employee;
import org.evomaster.client.java.controller.api.dto.database.execution.epa.RestActionDto;
import org.evomaster.client.java.controller.api.dto.database.execution.epa.RestActionsDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class EmployeeController {
	List<Employee> employees = new ArrayList<>();
	private long nextId = 0;

	// get all employees
	@GetMapping("/employees")
	public ResponseEntity<List<Employee>> getAllEmployees(){
		return ResponseEntity.ok(employees);
	}		
	
	// create employee rest api
	@PostMapping("/employees")
	public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
		employee.setId(nextId);
		nextId++;
		employees.add(employee);
		return ResponseEntity.ok(employee);
	}
	
	// get employee by id rest api
	@GetMapping("/employees/{id}")
	public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
		Employee employee = getEmployee(id);
		return ResponseEntity.ok(employee);
	}

	// update employee rest api
	
	@PutMapping("/employees/{id}")
	public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee employeeDetails){
		Employee employee = getEmployee(id);

		employee.setFirstName(employeeDetails.getFirstName());
		employee.setLastName(employeeDetails.getLastName());
		employee.setEmailId(employeeDetails.getEmailId());

		return ResponseEntity.ok(employee);
	}
	
	// delete employee rest api
	@DeleteMapping("/employees/{id}")
	public ResponseEntity<Map<String, Boolean>> deleteEmployee(@PathVariable Long id){
		Employee employee = getEmployee(id);
		employees.remove(employee);

		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/employees")
	public ResponseEntity<Map<String, Boolean>> deleteAllEmployees(){
		employees = new ArrayList<>();

		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return ResponseEntity.ok(response);
	}

	private Employee getEmployee(Long id) {
		Optional<Employee> employee = employees.stream().filter(e -> id.equals(e.getId())).findFirst();
		if (!employee.isPresent()) {
			throw new ResourceNotFoundException("Employee not exist with id :" + id);
		}
		return employee.get();
	}

	@GetMapping("/enabledEndpoints")
	public RestActionsDto getEnabledEndpoints() {
		RestActionsDto restActionsDto = new RestActionsDto();

		restActionsDto.enabledRestActions
				.add(new RestActionDto("get", "/employees"));
		restActionsDto.enabledRestActions
				.add(new RestActionDto("delete", "/employees"));
		restActionsDto.enabledRestActions
				.add(new RestActionDto("post", "/employees"));

		if (employees.size() > 0) {
			restActionsDto.enabledRestActions
					.add(new RestActionDto("get", "/employees/{id}"));
			restActionsDto.enabledRestActions
					.add(new RestActionDto("put", "/employees/{id}"));
			restActionsDto.enabledRestActions
					.add(new RestActionDto("delete", "/employees/{id}"));
		}

		return restActionsDto;
	}

}
