package com.rts.tap.serviceimplementation;
 
import java.util.List;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
 
import com.rts.tap.constants.MessageConstants;
import com.rts.tap.dao.BusinessUnitDao;
import com.rts.tap.dao.EmployeeDao;
import com.rts.tap.dao.RoleDao;
import com.rts.tap.exception.EmailValidationException;
import com.rts.tap.exception.EmployeeNotFoundException;
import com.rts.tap.exception.InvalidArgumentException;
import com.rts.tap.exception.RoleNotFoundException;
import com.rts.tap.model.Employee;
import com.rts.tap.model.Employee.EmploymentStatus;
import com.rts.tap.service.EmployeeService;
 
@Service
public class EmployeeServiceImpl implements EmployeeService {
 
	private EmployeeDao employeeDao;
 
	private BusinessUnitDao businessUnitDao;
 
	private RoleDao roleDao;
 
	private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);
 
	public EmployeeServiceImpl(EmployeeDao employeeDao, BusinessUnitDao businessUnitDao, RoleDao roleDao) {
		super();
		this.employeeDao = employeeDao;
		this.businessUnitDao = businessUnitDao;
		this.roleDao = roleDao;
	}
 
	@Override
	public List<Employee> getAllEmployee() {
		List<Employee> employees = employeeDao.getAllEmployee();
		if (employees.isEmpty()) {
			throw new EmployeeNotFoundException(MessageConstants.NO_EMPLOYEES_FOUND);
		}
		return employees;
	}
 
	@Override
	public List<Employee> getEmployeesByBusinessUnitHead(Long businessUnitHeadId) {
		if (businessUnitHeadId == null || businessUnitHeadId <= 0) {
			throw new InvalidArgumentException(
					String.format(MessageConstants.INVALID_BUSINESS_UNIT_HEAD_ID, businessUnitHeadId));
		}
 
		List<Employee> employees = employeeDao.getEmployeesByBusinessUnitHead(businessUnitHeadId);
		if (employees.isEmpty()) {
			logger.info(String.format(MessageConstants.NO_EMPLOYEES_FOUND_BU_HEAD, businessUnitHeadId));
			throw new EmployeeNotFoundException(
					String.format(MessageConstants.NO_EMPLOYEES_FOUND_BU_HEAD, businessUnitHeadId));
		}
		return employees;
	}
 
	@Override
	public String updateEmployee(Long employeeId, Employee updatedEmployee) {
		if (employeeId == null || employeeId <= 0) {
			throw new InvalidArgumentException(String.format(MessageConstants.INVALID_EMPLOYEE_ID, employeeId));
		}
 
		Employee existingEmployee = employeeDao.getEmployeeById(employeeId);
		if (existingEmployee == null) {
			throw new EmployeeNotFoundException(String.format(MessageConstants.EMPLOYEE_NOT_FOUND, employeeId));
		}
 
		logger.info("Updating employee with ID: {}", employeeId);
		existingEmployee.setEmployeeName(updatedEmployee.getEmployeeName());
		existingEmployee.setWorkLocation(updatedEmployee.getWorkLocation());
		existingEmployee.setEmployeeEmail(updatedEmployee.getEmployeeEmail());
		existingEmployee.setManagerId(updatedEmployee.getManagerId());
		existingEmployee.setRole(roleDao.getRoleById(updatedEmployee.getRole().getRoleId()));
		existingEmployee.setBusinessUnit(businessUnitDao.getBusinessUnitByLocation(updatedEmployee.getWorkLocation()));
		employeeDao.updateEmployee(existingEmployee);
		return MessageConstants.SUCCESS_MESSAGE_EMPLOYEE_ADDED;
	}
 
	@Override
	public List<Employee> getEmployeesByRoles() {
		List<Employee> employees = employeeDao.getEmployeesByRoles();
		if (employees.isEmpty()) {
			throw new EmployeeNotFoundException(MessageConstants.NO_EMPLOYEES_FOUND);
		}
		return employees;
	}
 
	@Override
	public List<String> getAllEmployeeEmail() {
		List<String> emails = employeeDao.getAllEmployeeByEmails();
		if (emails.isEmpty()) {
			throw new EmployeeNotFoundException(MessageConstants.NO_EMPLOYEE_EMAIL_ADDRESSES_FOUND);
		}
		return emails;
	}
 
	@Override
	public void updateEmployeeStatus(Long employeeId, EmploymentStatus status) {
		Employee employee = employeeDao.getEmployeeById(employeeId);
		if (employee == null) {
			throw new EmployeeNotFoundException(String.format(MessageConstants.EMPLOYEE_NOT_FOUND, employeeId));
		}
 
		if (!employee.getEmployeeStatus().equals(status)) {
			employee.setEmployeeStatus(status);
			employeeDao.updateEmployee(employee);
			logger.info("Updated employee status for ID {} to {}", employeeId, status);
		} else {
			logger.warn("Employee status for ID {} is already {}", employeeId, status);
		}
	}
 
	@Override
	public String checkEmployeeStatus(String email) {
		if (email == null || email.trim().isEmpty()) {
			throw new EmailValidationException(MessageConstants.EMAIL_CANNOT_BE_NULL);
		}
 
		String status = employeeDao.checkEmployeeStatus(email);
		if (status == null) {
			throw new EmployeeNotFoundException(String.format(MessageConstants.EMPLOYEE_STATUS_NOT_FOUND, email));
		}
		return status;
	}
 
	@Override
	public boolean existsByEmail(String email) {
		if (email == null || email.trim().isEmpty()) {
			throw new EmailValidationException(MessageConstants.EMAIL_CANNOT_BE_NULL);
		}
		return employeeDao.existsByEmail(email);
	}
 
	@Override
	public List<Employee> getEmployeesByRoleName(String roleName) {
		if (roleName == null || roleName.trim().isEmpty()) {
			throw new InvalidArgumentException(MessageConstants.ROLE_NAME_CANNOT_BE_NULL);
		}
 
		List<Employee> employees = employeeDao.findEmployeesByRoleName(roleName);
		if (employees.isEmpty()) {
			throw new RoleNotFoundException(String.format(MessageConstants.NO_EMPLOYEES_FOUND_FOR_ROLE, roleName));
		}
		return employees;
	}
 
	@Override
	public List<Employee> getEmployeesByRecruiter(String roleName, Long employeeId) {
		if (roleName == null || roleName.trim().isEmpty()) {
			throw new InvalidArgumentException(MessageConstants.ROLE_NAME_CANNOT_BE_NULL);
		}
		if (employeeId == null || employeeId <= 0) {
			throw new InvalidArgumentException(String.format(MessageConstants.INVALID_EMPLOYEE_ID, employeeId));
		}
 
		List<Employee> employees = employeeDao.findEmployeesByRecruiter(roleName, employeeId);
		if (employees.isEmpty()) {
			throw new EmployeeNotFoundException(
					String.format(MessageConstants.NO_EMPLOYEES_FOUND_FOR_RECRUITER, employeeId));
		}
		return employees;
	}
 
	@Override
	public List<Employee> getClientPartnerByBUHead(Long buHeadId) {
		if (buHeadId == null || buHeadId <= 0) {
			throw new InvalidArgumentException(String.format(MessageConstants.INVALID_BUSINESS_UNIT_HEAD_ID, buHeadId));
		}
 
		List<Employee> partners = employeeDao.findClientPartnerByBUHead(buHeadId);
		if (partners.isEmpty()) {
			throw new EmployeeNotFoundException(String.format(MessageConstants.NO_CLIENT_PARTNER_FOUND, buHeadId));
		}
		return partners;
	}
 
	@Override
	public List<Employee> getRecruitingManagerByClientPartner(Long buHeadId) {
		if (buHeadId == null || buHeadId <= 0) {
			throw new InvalidArgumentException(String.format(MessageConstants.INVALID_BUSINESS_UNIT_HEAD_ID, buHeadId));
		}
 
		List<Employee> managers = employeeDao.findRecruitingManagerByClientPartner(buHeadId);
		if (managers.isEmpty()) {
			throw new EmployeeNotFoundException(String.format(MessageConstants.NO_RECRUITING_MANAGER_FOUND, buHeadId));
		}
		return managers;
	}
 
	@Override
	public List<Employee> findRecruiterByRecruitingManager(Long employeeId) {
		if (employeeId == null || employeeId <= 0) {
			throw new InvalidArgumentException(String.format(MessageConstants.INVALID_EMPLOYEE_ID, employeeId));
		}
 
		List<Employee> recruiters = employeeDao.findRecruiterByRecruitingManager(employeeId);
		if (recruiters.isEmpty()) {
			throw new EmployeeNotFoundException(String.format(MessageConstants.NO_RECRUITERS_FOUND, employeeId));
		}
		return recruiters;
	}
 
	@Override
	public List<Employee> getClientPartnerByBU(Long employeeId) {
		if (employeeId == null || employeeId <= 0) {
			throw new InvalidArgumentException(String.format(MessageConstants.INVALID_EMPLOYEE_ID, employeeId));
		}
 
		List<Employee> partners = employeeDao.getClientPartnerByBU(employeeId);
		if (partners.isEmpty()) {
			throw new EmployeeNotFoundException(String.format(MessageConstants.NO_CLIENT_PARTNER_FOUND, employeeId));
		}
		return partners;
	}
 
	@Override
	public Employee getEmployeeById(Long employeeId) {
		if (employeeId == null || employeeId <= 0) {
			throw new InvalidArgumentException(String.format(MessageConstants.INVALID_EMPLOYEE_ID, employeeId));
		}
 
		Employee employee = employeeDao.getEmployeeById(employeeId);
		if (employee == null) {
			throw new EmployeeNotFoundException(String.format(MessageConstants.EMPLOYEE_NOT_FOUND, employeeId));
		}
		return employee;
	}
 
}
 
 