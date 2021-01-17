package com.paypal.bfs.test.employeeserv.impl;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.paypal.bfs.test.employeeserv.api.EmployeeResource;
import com.paypal.bfs.test.employeeserv.api.Entity.Employee;
import com.paypal.bfs.test.employeeserv.exception.NoDataFoundException;
import com.paypal.bfs.test.employeeserv.repository.EmployeeRepository;

/**
 * Implementation class for employee resource.
 */
@RestController
public class EmployeeResourceImpl implements EmployeeResource {

	@Autowired
	private EmployeeRepository employeeRepository;

	@Override
	public ResponseEntity<Employee> employeeGetById(@PathVariable("id") String id) {
		Optional<Employee> employee = employeeRepository.findById(Long.valueOf(id));
		if (employee.isPresent()) {
			return new ResponseEntity<>(employee.get(), HttpStatus.OK);
		} else {
			throw new NoDataFoundException("No Employee Found for employee Id : " + id);
		}
	}

	@Override
	public ResponseEntity<Employee> createEmployee(@Valid @RequestBody Employee employee) {
		Employee dbemployee = employeeRepository.save(employee);
		return new ResponseEntity<>(dbemployee, HttpStatus.CREATED);
	}

}
