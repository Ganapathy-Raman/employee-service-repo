package com.rts.tap.service;
 
import java.util.List;
 
import com.rts.tap.model.Employee;

import com.rts.tap.model.Employee.EmploymentStatus;
 
public interface EmployeeService {
 
 
	List<Employee> getAllEmployee();
 
	List<Employee> getEmployeesByBusinessUnitHead(Long employeeId);
 
	List<String> getAllEmployeeEmail();
 
	Employee getEmployeeById(Long employeeId);
 
	String updateEmployee(Long employeeId, Employee employee);
 
	List<Employee> getEmployeesByRoleName(String roleName);
 
	List<Employee> getEmployeesByRoles();
 
	void updateEmployeeStatus(Long employeeId, EmploymentStatus employeeStatus);
 
	String checkEmployeeStatus(String email);
 
	boolean existsByEmail(String email);
 
	List<Employee> getEmployeesByRecruiter(String roleName, Long employeeId);
 
	List<Employee> getClientPartnerByBUHead(Long buHeadId);
 
	List<Employee> getRecruitingManagerByClientPartner(Long buHeadId);
 
	List<Employee> findRecruiterByRecruitingManager(Long employeeId);
 
	List<Employee> getClientPartnerByBU(Long employeeId);
 
}

 