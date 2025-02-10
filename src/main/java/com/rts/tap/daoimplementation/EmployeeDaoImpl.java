package com.rts.tap.daoimplementation;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.rts.tap.constants.MessageConstants;
import com.rts.tap.dao.EmployeeDao;
import com.rts.tap.exception.EmailCheckException;
import com.rts.tap.exception.EmployeeNotFoundException;
import com.rts.tap.exception.EmployeeRetrivalException;
import com.rts.tap.model.BusinessUnit;
import com.rts.tap.model.Documents;
import com.rts.tap.model.Employee;
import com.rts.tap.model.Employee.EmploymentStatus;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

@Repository
@Transactional
public class EmployeeDaoImpl implements EmployeeDao {

	private static final Logger logger = LoggerFactory.getLogger(EmployeeDaoImpl.class);

	private static final String EMAIL_PARAMETER = "email";

	private static final String ROLE_PARAMETER = "roleName";

	private EntityManager entityManager;

	public EmployeeDaoImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Employee> getAllEmployee() {
		String hql = "from Employee";
		Query query = entityManager.createQuery(hql);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Employee> getEmployeesByBusinessUnitHead(Long employeeId) {

		String businessUnitHQL = "SELECT e.businessUnit FROM Employee e WHERE e.employeeId = :employeeId";
		BusinessUnit businessUnit = (BusinessUnit) entityManager.createQuery(businessUnitHQL)
				.setParameter("employeeId", employeeId).getSingleResult();

		String hql = "FROM Employee e WHERE e.businessUnit.businessunitId = :businessUnitId";
		Query query = entityManager.createQuery(hql);
		query.setParameter("businessUnitId", businessUnit.getBusinessunitId());
		return query.getResultList();
	}

	public void updateEmployee(Employee employee) {
		entityManager.merge(employee);
	}

	public Employee getEmployeeById(Long id) {

		if (id == null) {
			logger.error(MessageConstants.INVALID_EMPLOYEE_ID, id);
			throw new IllegalArgumentException(MessageConstants.INVALID_EMPLOYEE_ID + " cannot be null.");
		}

		Employee employee = entityManager.find(Employee.class, id);
		if (employee == null) {
			logger.error(MessageConstants.EMPLOYEE_NOT_FOUND, id);
			throw new EmployeeNotFoundException(String.format(MessageConstants.EMPLOYEE_NOT_FOUND, id));
		}
		return employee;
	}

	@Override
	public Employee findEmployeeByEmail(String email) {
		if (email == null || email.isEmpty()) {
			throw new IllegalArgumentException(MessageConstants.EMAIL_CANNOT_BE_NULL);
		}

		String hql = "FROM Employee WHERE employeeEmail = :email";
		TypedQuery<Employee> query = entityManager.createQuery(hql, Employee.class);
		query.setParameter(EMAIL_PARAMETER, email);
		try {
			return query.getSingleResult();
		} catch (NoResultException e) {

			String errorMessage = String.format(MessageConstants.EMPLOYEE_NOT_FOUND, email);
			throw new EmployeeNotFoundException(errorMessage);
		} catch (Exception e) {

			logger.error("Error retrieving employee with email: {}", email, e);

			throw new EmployeeRetrivalException(MessageConstants.ERROR_PROCESSING_EMPLOYEE);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getAllEmployeeByEmails() {
		String hql = "select e.employeeEmail from Employee e";
		Query query = entityManager.createQuery(hql, String.class);
		return query.getResultList();
	}

	@Override
	public boolean existsByEmail(String email) {
		if (email == null || email.isEmpty()) {
			throw new IllegalArgumentException(MessageConstants.EMAIL_CANNOT_BE_NULL);
		}

		try {
			String query = "SELECT COUNT(e) > 0 FROM Employee e WHERE e.employeeEmail = :email";
			Boolean exists = entityManager.createQuery(query, Boolean.class).setParameter(EMAIL_PARAMETER, email)
					.getSingleResult();

			return exists != null && exists;

		} catch (Exception e) {

			String errorMessage = String.format(MessageConstants.ERROR_CHECKING_EMAIL, email);
			throw new EmailCheckException(errorMessage, e);
		}
	}

	@Override
	public Optional<Employee> findByempId(Long employeeId) {
		try {

			Employee employee = entityManager.find(Employee.class, employeeId);

			return Optional.ofNullable(employee);
		} catch (Exception e) {

			logger.error("Error fetching employee with ID: {}", employeeId, e);
			return Optional.empty();
		}
	}

	@Override
	public String checkEmployeeStatus(String email) {
		String hql = "FROM Employee WHERE employeeEmail = :email";
		TypedQuery<Employee> query = entityManager.createQuery(hql, Employee.class);
		query.setParameter(EMAIL_PARAMETER, email);
		try {
			Employee employee = query.getSingleResult();
			EmploymentStatus status = employee.getEmployeeStatus();

			if (status == EmploymentStatus.ACTIVE) {
				return "ACTIVE";
			} else if (status == EmploymentStatus.INACTIVE) {
				return "INACTIVE";
			} else {
				return "Status unknown";
			}
		} catch (NoResultException e) {
			return "Employee not found";
		}
	}

	@Override
	public List<Employee> findEmployeesByRoleName(String roleName) {
		String hql = "SELECT e FROM Employee e WHERE e.role.roleName = :roleName";
		TypedQuery<Employee> query = entityManager.createQuery(hql, Employee.class);
		query.setParameter(ROLE_PARAMETER, roleName);
		return query.getResultList();
	}

	@Override
	public List<Employee> findEmployeesByRecruiter(String roleName, Long employeeId) {

		Employee employee = entityManager.find(Employee.class, employeeId);

		if (employee == null) {
			return Collections.emptyList();
		}

		Long businessUnitId = employee.getBusinessUnit().getBusinessunitId();

		if (businessUnitId != null) {

			TypedQuery<Employee> query = entityManager.createQuery(
					"SELECT e FROM Employee e WHERE e.businessUnit.businessunitId = :businessUnitId AND e.role.roleName = :roleName",
					Employee.class);

			query.setParameter("businessUnitId", businessUnitId);
			query.setParameter(ROLE_PARAMETER, roleName);
			return query.getResultList();
		} else {
			logger.warn("No ID Found!");
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Employee> getEmployeesByRoles() {
		String hql = "select emp from Employee emp where emp.role.roleName = 'Recruiter Manager' or emp.role.roleName = 'Client Partner'";
		Query query = entityManager.createQuery(hql);
		return query.getResultList();
	}

	public List<Employee> findEmployeesByBUHead(Long buHeadId) {
		String hql = "select emp from Employee emp " + "left join fetch emp.businessUnit bu "
				+ "left join fetch emp.manager m " + "where bu.managerId = :buHeadId "
				+ "and (m.role.name = 'ClientPartner' " + "     or m.role.name = 'RecruitingManager' "
				+ "     or m.role.name = 'Recruiter')";
		TypedQuery<Employee> query = entityManager.createQuery(hql, Employee.class);
		query.setParameter("buHeadId", buHeadId);
		return query.getResultList();
	}

	@Override
	public List<Employee> findClientPartnerByBUHead(Long buHeadId) {
		String hql = "select emp from Employee emp where emp.managerId = :buHeadId and emp.role.roleName = 'Client Partner'";
		TypedQuery<Employee> query = entityManager.createQuery(hql, Employee.class);
		query.setParameter("buHeadId", buHeadId);
		return query.getResultList();
	}

	@Override
	public List<Employee> findRecruitingManagerByClientPartner(Long clientPartnerId) {
		String hql = "select emp from Employee emp where emp.managerId = :clientPartnerId and emp.role.roleName = 'Recruiting Manager'";
		TypedQuery<Employee> query = entityManager.createQuery(hql, Employee.class);
		query.setParameter("clientPartnerId", clientPartnerId);
		return query.getResultList();
	}

	@Override
	public List<Employee> findRecruiterByRecruitingManager(Long recruitingManagerId) {
		String hql = "select emp from Employee emp where emp.managerId = :recruitingManagerId and emp.role.roleName = 'Recruiter'";
		TypedQuery<Employee> query = entityManager.createQuery(hql, Employee.class);
		query.setParameter("recruitingManagerId", recruitingManagerId);
		return query.getResultList();
	}

	@Override
	public void saveComplete(Employee employee) {
		logger.info("Saving employee with ID: {}", employee.getEmployeeId());
		if (employee.getEmployeeId() != null) {

			logger.info("Updating employee with ID: {}", employee.getEmployeeId());
			entityManager.merge(employee);
		} else {

			logger.info("Inserting new employee: {}", employee);
			entityManager.persist(employee);
		}
	}

	@Override

	public Documents saveDocuments(Documents document) {
		entityManager.persist(document);
		return document;
	}

	@Override
	public List<Long> emplgetAllDocumentIdByEmployeeId(Long employeeId) {
		logger.info("Fetching documents for employee with ID: {}", employeeId);

		Employee employee = entityManager.find(Employee.class, employeeId);
		if (employee != null) {
			logger.info("Employee found with ID: {}", employeeId);
			return employee.getDocuments().stream().map(Documents::getDocumentsId)

					.toList();
		} else {
			logger.warn("No employee found with ID: {}", employeeId);
		}
		return List.of();
	}

	@Override
	public Documents findByDocumentId(Long documentId) {
		if (documentId == null) {
			return null;
		}

		TypedQuery<Documents> query = entityManager
				.createQuery("SELECT d FROM Documents d WHERE d.documentsId = :documentId", Documents.class);
		query.setParameter("documentId", documentId);

		return query.getResultStream().findFirst().orElse(null);
	}

	@Override
	public void saveProfile(Employee employee) {
		entityManager.merge(employee);

	}

	@Override
	public List<Employee> getClientPartnerByBU(Long employeeId) {
		TypedQuery<Employee> query = entityManager.createQuery(
				"SELECT e FROM Employee e WHERE e.managerId = :managerId AND e.role.roleName = :roleName",
				Employee.class);
		query.setParameter("managerId", employeeId);
		query.setParameter(ROLE_PARAMETER, "Client Partner");
		return query.getResultList();
	}

}
