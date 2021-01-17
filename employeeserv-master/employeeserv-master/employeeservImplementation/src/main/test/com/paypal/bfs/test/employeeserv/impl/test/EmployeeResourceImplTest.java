package com.paypal.bfs.test.employeeserv.impl.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paypal.bfs.test.employeeserv.api.Entity.Address;
import com.paypal.bfs.test.employeeserv.api.Entity.Employee;
import com.paypal.bfs.test.employeeserv.impl.EmployeeResourceImpl;
import com.paypal.bfs.test.employeeserv.repository.EmployeeRepository;

@RunWith(MockitoJUnitRunner.class)
public class EmployeeResourceImplTest {

	private MockMvc mockMvc;

	@InjectMocks
	EmployeeResourceImpl employeeResourceImpl;

	@Mock
	EmployeeRepository employeeRepository;

	private ObjectMapper mapper = new ObjectMapper();

	@Before
	public void setUp() throws Exception {
		
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(employeeResourceImpl)
				.setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver()).build();
	}

	@Test
	public void testEmployeeGetById_Found() throws Exception {
		Mockito.when(employeeRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(getEmployee()));
		this.mockMvc.perform(get("/v1/bfs/employees/{id}", 1).accept(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk());
	}
	
	@Test
	public void testEmployeeGetById_NotFound() throws Exception {
		Mockito.when(employeeRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
		this.mockMvc.perform(get("/v1/bfs/employees/{id}", 1).accept(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isNotFound());
	}

	@Test
	public void valid_testCreateEmployee() throws Exception {
		Mockito.when(employeeRepository.save(Mockito.any())).thenReturn(getEmployee());

		String body = mapper.writeValueAsString(getEmployee());
		this.mockMvc.perform(post("/v1/bfs/employees").contentType(MediaType.APPLICATION_JSON).content(body))
				.andExpect(status().isCreated());
	}
	
	@Test
	public void invalid_employee_CreateEmployee() throws Exception {
		Employee employee = getEmployee();
		employee.setFirstName(null);
		String body = mapper.writeValueAsString(employee);
		this.mockMvc.perform(post("/v1/bfs/employees").contentType(MediaType.APPLICATION_JSON).content(body))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	public void invalid_address_testCreateEmployee() throws Exception {
		Employee employee = getEmployee();
		employee.getAddress().setLine1(null);
		String body = mapper.writeValueAsString(employee);
		this.mockMvc.perform(post("/v1/bfs/employees").contentType(MediaType.APPLICATION_JSON).content(body))
				.andExpect(status().isBadRequest());
	}
	
	private Employee getEmployee() {
		
		Employee employee = new Employee();
		employee.setFirstName("David");
		employee.setLastName("Arter");
		employee.setAddress(getAddress());
		return employee;
	}
	
	private Address getAddress()
	{
		Address address = new Address();
		address.setCity("St. Louis");
		address.setState("Missouri");
		address.setCountry("USA");
		address.setLine1("line1");
		address.setLine2("line2");
		address.setZipCode("43535");
		address.setId(1l);
		
		
		return address;
	}
	
	
}
