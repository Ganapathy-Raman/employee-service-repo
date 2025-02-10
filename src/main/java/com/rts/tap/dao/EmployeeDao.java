package com.rts.tap.dao;
 
import java.util.List;

import java.util.Optional;
 
import com.rts.tap.model.Documents;

import com.rts.tap.model.Employee;
 
public interface EmployeeDao {
 
 
	List<String> getAllEmployeeByEmails();
 
	List<Employee> getAllEmployee();
 
	List<Employee> getEmployeesByBusinessUnitHead(Long employeeId);
 
	void updateEmployee(Employee employee);
 
	List<Employee> findEmployeesByRoleName(String roleName);
 
	Employee getEmployeeById(Long employeeId);
 
	String checkEmployeeStatus(String email);
 
	boolean existsByEmail(String employeeEmail);
 
	List<Employee> getEmployeesByRoles();
 
	Employee findEmployeeByEmail(String email);
 
	Optional<Employee> findByempId(Long employeeId);
 
	List<Employee> findEmployeesByRecruiter(String roleName, Long employeeId);
 
	List<Employee> findClientPartnerByBUHead(Long buHeadId);
 
	List<Employee> findRecruitingManagerByClientPartner(Long buHeadId);
 
	List<Employee> findRecruiterByRecruitingManager(Long employeeId);
 
	void saveComplete(Employee existingEmployee);
 
	Documents saveDocuments(Documents document);
 
	public List<Long> emplgetAllDocumentIdByEmployeeId(Long employeeId);
 
	Documents findByDocumentId(Long documentId);
 
	void saveProfile(Employee employee);
 
	public List<Employee> getClientPartnerByBU(Long employeeId);

}

 