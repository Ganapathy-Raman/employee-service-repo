package com.rts.tap.serviceimplementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.rts.tap.dao.BusinessUnitDao;
import com.rts.tap.dao.EmployeeDao;
import com.rts.tap.dao.RoleDao;
import com.rts.tap.exception.EmailValidationException;
import com.rts.tap.exception.EmployeeNotFoundException;
import com.rts.tap.exception.InvalidArgumentException;
import com.rts.tap.exception.RoleNotFoundException;
import com.rts.tap.model.Employee;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

	@Mock
	private EmployeeDao employeeDao;

	@Mock
	private BusinessUnitDao businessUnitDao;

	@Mock
	private RoleDao roleDao;

	@InjectMocks
	private EmployeeServiceImpl employeeService;

	private Employee testEmployee;
	private String testEmail = "test@example.com";
	private Long testEmployeeId = 1L;

	@BeforeEach
	void setUp() {
		testEmployee = new Employee();
		testEmployee.setEmployeeEmail(testEmail);
		testEmployee.setEmployeeName("John Doe");
		testEmployee.setEmployeeStatus(Employee.EmploymentStatus.ACTIVE);
		testEmployee.setEmployeeId(testEmployeeId);
	}

	@Test
	void testGetAllEmployee() {
		List<Employee> mockEmployees = Arrays.asList(testEmployee);
		when(employeeDao.getAllEmployee()).thenReturn(mockEmployees);

		List<Employee> employees = employeeService.getAllEmployee();

		assertNotNull(employees);
		assertEquals(1, employees.size());
		assertEquals(testEmployee.getEmployeeName(), employees.get(0).getEmployeeName());
	}

	@Test
	void testGetAllEmployee_EmptyList() {
		when(employeeDao.getAllEmployee()).thenReturn(Collections.emptyList());

		assertThrows(EmployeeNotFoundException.class, employeeService::getAllEmployee);
	}

	@Test
	void testGetEmployeesByBusinessUnitHead_NullId() {
		assertThrows(InvalidArgumentException.class, () -> employeeService.getEmployeesByBusinessUnitHead(null));
	}

	@Test
	void testGetEmployeesByBusinessUnitHead_Success() {
		Long businessUnitHeadId = 1L;
		when(employeeDao.getEmployeesByBusinessUnitHead(businessUnitHeadId))
				.thenReturn(Collections.singletonList(testEmployee));

		List<Employee> employees = employeeService.getEmployeesByBusinessUnitHead(businessUnitHeadId);

		assertNotNull(employees);
		assertFalse(employees.isEmpty());
	}

	@Test
	void testGetEmployeesByBusinessUnitHead_EmptyList() {
		when(employeeDao.getEmployeesByBusinessUnitHead(anyLong())).thenReturn(Collections.emptyList());

		assertThrows(EmployeeNotFoundException.class, () -> employeeService.getEmployeesByBusinessUnitHead(1L));
	}

	@Test
	void testUpdateEmployee_NotFound() {
		when(employeeDao.getEmployeeById(testEmployeeId)).thenReturn(null);

		assertThrows(EmployeeNotFoundException.class,
				() -> employeeService.updateEmployee(testEmployeeId, new Employee()));
	}

	@Test
	void testGetEmployeeById() {
		when(employeeDao.getEmployeeById(testEmployeeId)).thenReturn(testEmployee);

		Employee found = employeeService.getEmployeeById(testEmployeeId);

		assertEquals(testEmployee, found);
	}

	@Test
	void testGetEmployeeById_NotFound() {
		when(employeeDao.getEmployeeById(testEmployeeId)).thenReturn(null);

		assertThrows(EmployeeNotFoundException.class, () -> employeeService.getEmployeeById(testEmployeeId));
	}

	@Test
	void testUpdateEmployeeStatus_Success() {
		when(employeeDao.getEmployeeById(testEmployeeId)).thenReturn(testEmployee);

		employeeService.updateEmployeeStatus(testEmployeeId, Employee.EmploymentStatus.INACTIVE);

		assertEquals(Employee.EmploymentStatus.INACTIVE, testEmployee.getEmployeeStatus());
		verify(employeeDao).updateEmployee(testEmployee);
	}

	@Test
	void testUpdateEmployeeStatus_AlreadySame() {
		when(employeeDao.getEmployeeById(testEmployeeId)).thenReturn(testEmployee);

		employeeService.updateEmployeeStatus(testEmployeeId, Employee.EmploymentStatus.ACTIVE);

		assertEquals(Employee.EmploymentStatus.ACTIVE, testEmployee.getEmployeeStatus());
		verify(employeeDao, never()).updateEmployee(testEmployee);
	}

	@Test
	void testGetEmployeesByRoles_Success() {
		when(employeeDao.getEmployeesByRoles()).thenReturn(Arrays.asList(testEmployee));

		List<Employee> result = employeeService.getEmployeesByRoles();

		assertNotNull(result);
		assertFalse(result.isEmpty());
		assertEquals(testEmployee, result.get(0));
	}

	@Test
	void testGetEmployeesByRoles_NoEmployees() {
		when(employeeDao.getEmployeesByRoles()).thenReturn(Collections.emptyList());

		assertThrows(EmployeeNotFoundException.class, () -> employeeService.getEmployeesByRoles());
	}

	@Test
	void testGetAllEmployeeEmail_Success() {
		List<String> expectedEmails = Arrays.asList("john@example.com", "jane@example.com");
		when(employeeDao.getAllEmployeeByEmails()).thenReturn(expectedEmails);

		List<String> result = employeeService.getAllEmployeeEmail();

		assertNotNull(result);
		assertEquals(2, result.size());
	}

	@Test
	void testGetAllEmployeeEmail_NoEmails() {
		when(employeeDao.getAllEmployeeByEmails()).thenReturn(Collections.emptyList());

		assertThrows(EmployeeNotFoundException.class, () -> employeeService.getAllEmployeeEmail());
	}

	@Test
	void testGetEmployeesByRoleName_Success() {
		String roleName = "Developer";
		List<Employee> employees = Arrays.asList(testEmployee);
		when(employeeDao.findEmployeesByRoleName(roleName)).thenReturn(employees);

		List<Employee> result = employeeService.getEmployeesByRoleName(roleName);

		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(testEmployee, result.get(0));
	}

	@Test
	void testGetEmployeesByRoleName_EmptyList() {
		String roleName = "Developer";
		when(employeeDao.findEmployeesByRoleName(roleName)).thenReturn(Collections.emptyList());

		assertThrows(RoleNotFoundException.class, () -> employeeService.getEmployeesByRoleName(roleName));
	}

	@Test
	void testGetEmployeesByRecruiter_Success() {
		String roleName = "Developer";
		Long employeeId = 1L;
		List<Employee> expectedEmployees = Collections.singletonList(testEmployee);
		when(employeeDao.findEmployeesByRecruiter(roleName, employeeId)).thenReturn(expectedEmployees);

		List<Employee> result = employeeService.getEmployeesByRecruiter(roleName, employeeId);

		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(testEmployee, result.get(0));
	}

	@Test
	void testGetEmployeesByRecruiter_EmptyList() {
		String roleName = "Developer";
		Long employeeId = 1L;
		when(employeeDao.findEmployeesByRecruiter(roleName, employeeId)).thenReturn(Collections.emptyList());

		assertThrows(EmployeeNotFoundException.class,
				() -> employeeService.getEmployeesByRecruiter(roleName, employeeId));
	}

	@Test
	void testGetClientPartnerByBUHead_Success() {
		Long buHeadId = 1L;
		List<Employee> expectedEmployees = Collections.singletonList(testEmployee);
		when(employeeDao.findClientPartnerByBUHead(buHeadId)).thenReturn(expectedEmployees);

		List<Employee> result = employeeService.getClientPartnerByBUHead(buHeadId);

		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(testEmployee, result.get(0));
	}

	@Test
	void testGetClientPartnerByBUHead_EmptyList() {
		Long buHeadId = 1L;
		when(employeeDao.findClientPartnerByBUHead(buHeadId)).thenReturn(Collections.emptyList());

		assertThrows(EmployeeNotFoundException.class, () -> employeeService.getClientPartnerByBUHead(buHeadId));
	}

	@Test
	void testGetRecruitingManagerByClientPartner_Success() {
		Long buHeadId = 1L;
		Employee manager = new Employee();
		manager.setEmployeeName("Manager");
		List<Employee> expectedManagers = Collections.singletonList(manager);
		when(employeeDao.findRecruitingManagerByClientPartner(buHeadId)).thenReturn(expectedManagers);

		List<Employee> result = employeeService.getRecruitingManagerByClientPartner(buHeadId);

		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(manager, result.get(0));
	}

	@Test
	void testGetRecruitingManagerByClientPartner_EmptyList() {
		Long buHeadId = 1L;
		when(employeeDao.findRecruitingManagerByClientPartner(buHeadId)).thenReturn(Collections.emptyList());

		assertThrows(EmployeeNotFoundException.class,
				() -> employeeService.getRecruitingManagerByClientPartner(buHeadId));
	}

	@Test
	void testFindRecruiterByRecruitingManager_Success() {
		Long recruitingManagerId = 1L;
		Employee recruiter = new Employee();
		recruiter.setEmployeeName("Recruiter One");
		List<Employee> expectedRecruiters = Collections.singletonList(recruiter);
		when(employeeDao.findRecruiterByRecruitingManager(recruitingManagerId)).thenReturn(expectedRecruiters);

		List<Employee> result = employeeService.findRecruiterByRecruitingManager(recruitingManagerId);

		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(recruiter, result.get(0));
	}

	@Test
	void testFindRecruiterByRecruitingManager_EmptyList() {
		Long recruitingManagerId = 1L;
		when(employeeDao.findRecruiterByRecruitingManager(recruitingManagerId)).thenReturn(Collections.emptyList());

		assertThrows(EmployeeNotFoundException.class,
				() -> employeeService.findRecruiterByRecruitingManager(recruitingManagerId));
	}

	@Test
	void testGetClientPartnerByBU_Success() {
		Long employeeId = 1L;
		List<Employee> expectedPartners = Collections.singletonList(testEmployee);
		when(employeeDao.getClientPartnerByBU(employeeId)).thenReturn(expectedPartners);

		List<Employee> result = employeeService.getClientPartnerByBU(employeeId);

		assertNotNull(result);
		assertFalse(result.isEmpty());
		assertEquals(testEmployee, result.get(0));
	}

	@Test
	void testGetClientPartnerByBU_EmptyList() {
		Long employeeId = 1L;
		when(employeeDao.getClientPartnerByBU(employeeId)).thenReturn(Collections.emptyList());

		assertThrows(EmployeeNotFoundException.class, () -> employeeService.getClientPartnerByBU(employeeId));
	}

	@Test
	void testGetAllEmployee_Success() {
		List<Employee> employees = Collections.singletonList(testEmployee);
		when(employeeDao.getAllEmployee()).thenReturn(employees);

		List<Employee> result = employeeService.getAllEmployee();

		assertNotNull(result);
		assertEquals(1, result.size());
	}

	@Test
	void testGetEmployeesByBusinessUnitHead_InvalidId() {
		Long employeeId = -1L;

		assertThrows(InvalidArgumentException.class, () -> employeeService.getEmployeesByBusinessUnitHead(employeeId));
	}

	@Test
	void testGetEmployeeById_Success() {
		when(employeeDao.getEmployeeById(1L)).thenReturn(testEmployee);

		Employee result = employeeService.getEmployeeById(1L);
		assertNotNull(result);
		assertEquals(testEmployee, result);
	}

	@Test
	void testGetEmployeesByRoles_EmptyList() {
		when(employeeDao.getEmployeesByRoles()).thenReturn(Collections.emptyList());

		assertThrows(EmployeeNotFoundException.class, () -> employeeService.getEmployeesByRoles());
	}

	@Test
	void testUpdateEmployeeStatus_NotFound() {
		when(employeeDao.getEmployeeById(1L)).thenReturn(null);

		assertThrows(EmployeeNotFoundException.class,
				() -> employeeService.updateEmployeeStatus(1L, Employee.EmploymentStatus.ACTIVE));
	}

	@Test
	void testCheckEmployeeStatus_Success() {
		when(employeeDao.checkEmployeeStatus(testEmployee.getEmployeeEmail())).thenReturn("ACTIVE");

		String result = employeeService.checkEmployeeStatus(testEmployee.getEmployeeEmail());
		assertEquals("ACTIVE", result);
	}

	@Test
	void testCheckEmployeeStatus_NotFound() {
		when(employeeDao.checkEmployeeStatus(testEmployee.getEmployeeEmail())).thenReturn(null);

		assertThrows(EmployeeNotFoundException.class,
				() -> employeeService.checkEmployeeStatus(testEmployee.getEmployeeEmail()));
	}

	@Test
	void testExistsByEmail_Success() {
		when(employeeDao.existsByEmail(anyString())).thenReturn(true);

		assertTrue(employeeService.existsByEmail("test@example.com"));
	}

	@Test
	void testExistsByEmail_EmailNullOrEmpty() {
		assertThrows(EmailValidationException.class, () -> employeeService.existsByEmail(null));
		assertThrows(EmailValidationException.class, () -> employeeService.existsByEmail(""));
	}

	@Test
	void testGetEmployeesByBusinessUnitHead_NegativeId() {
		assertThrows(InvalidArgumentException.class, () -> employeeService.getEmployeesByBusinessUnitHead(-1L));
	}

	@Test
	void testUpdateEmployee_InvalidId() {
		when(employeeDao.getEmployeeById(anyLong())).thenReturn(null);
		assertThrows(EmployeeNotFoundException.class, () -> employeeService.updateEmployee(1L, new Employee()));
	}

	@Test
	void testGetEmployeeById_InvalidId() {
		assertThrows(InvalidArgumentException.class, () -> employeeService.getEmployeeById(-1L));
	}

	@Test
	void testCheckEmployeeStatus_EmptyEmail() {
		assertThrows(EmailValidationException.class, () -> employeeService.checkEmployeeStatus(""));
	}

	@Test
	void testExistsByEmail_NullEmail() {
		assertThrows(EmailValidationException.class, () -> employeeService.existsByEmail(null));
	}

	@Test
	void testGetEmployeesByRoleName_NullRoleName() {
		assertThrows(InvalidArgumentException.class, () -> employeeService.getEmployeesByRoleName(null));
	}

	@Test
	void testGetEmployeesByRecruiter_InvalidRoleName() {
		assertThrows(InvalidArgumentException.class, () -> employeeService.getEmployeesByRecruiter("", 1L));
	}

	@Test
	void testGetEmployeesByRecruiter_InvalidEmployeeId() {
		assertThrows(InvalidArgumentException.class, () -> employeeService.getEmployeesByRecruiter("Developer", -1L));
	}

	@Test
	void testGetClientPartnerByBUHead_NullId() {
		assertThrows(InvalidArgumentException.class, () -> employeeService.getClientPartnerByBUHead(null));
	}

	@Test
	void testGetRecruitingManagerByClientPartner_NullId() {
		assertThrows(InvalidArgumentException.class, () -> employeeService.getRecruitingManagerByClientPartner(null));
	}

	@Test
	void testGetClientPartnerByBU_NullId() {
		assertThrows(InvalidArgumentException.class, () -> employeeService.getClientPartnerByBU(null));
	}

	@Test
	void testGetEmployeesByBusinessUnitHead_WhenEmployeesFound() {
		Long employeeId = 1L;
		when(employeeDao.getEmployeesByBusinessUnitHead(employeeId))
				.thenReturn(Collections.singletonList(testEmployee));

		List<Employee> result = employeeService.getEmployeesByBusinessUnitHead(employeeId);
		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(testEmployee, result.get(0));
	}

	@Test
	void testGetEmployeesByRoles_WhenEmployeesFound() {
		when(employeeDao.getEmployeesByRoles()).thenReturn(Collections.singletonList(testEmployee));

		List<Employee> result = employeeService.getEmployeesByRoles();
		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(testEmployee, result.get(0));
	}

	@Test
	void testCheckEmployeeStatus_WithDifferentEmail() {
		String email = "different@example.com";
		when(employeeDao.checkEmployeeStatus(email)).thenReturn("INACTIVE");

		String status = employeeService.checkEmployeeStatus(email);
		assertEquals("INACTIVE", status);
	}

	@Test
	void testGetEmployeesByRoleName_WithDifferentRole() {
		String roleName = "Manager";
		when(employeeDao.findEmployeesByRoleName(roleName)).thenReturn(Collections.singletonList(testEmployee));

		List<Employee> result = employeeService.getEmployeesByRoleName(roleName);
		assertNotNull(result);
		assertEquals(1, result.size());
	}

	@Test
	void testGetClientPartnerByBUHead_WhenMultiplePartnersFound() {
		List<Employee> partners = List.of(new Employee(), new Employee());
		when(employeeDao.findClientPartnerByBUHead(1L)).thenReturn(partners);

		List<Employee> result = employeeService.getClientPartnerByBUHead(1L);
		assertNotNull(result);
		assertEquals(2, result.size());
	}

	@Test
	void testGetEmployeesByRecruiter_SuccessScenario() {
		List<Employee> employees = List.of(testEmployee);
		when(employeeDao.findEmployeesByRecruiter("Developer", testEmployee.getEmployeeId())).thenReturn(employees);

		List<Employee> result = employeeService.getEmployeesByRecruiter("Developer", testEmployee.getEmployeeId());
		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(testEmployee, result.get(0));
	}

	@Test
	void testGetRecruitingManagerByClientPartner_SuccessScenario() {
		List<Employee> managers = Collections.singletonList(testEmployee);
		when(employeeDao.findRecruitingManagerByClientPartner(testEmployee.getEmployeeId())).thenReturn(managers);

		List<Employee> result = employeeService.getRecruitingManagerByClientPartner(testEmployee.getEmployeeId());
		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(testEmployee, result.get(0));
	}

}