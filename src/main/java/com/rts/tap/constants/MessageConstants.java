
package com.rts.tap.constants;

import java.util.logging.Logger;

import org.slf4j.LoggerFactory;

public class MessageConstants {

	private MessageConstants() {

		throw new AssertionError("Cannot instantiate MessageConstants class");

	}

	public static final Logger LOGGER = (Logger) LoggerFactory.getLogger(MessageConstants.class);

	public static final String SUCCESS_MESSAGE = "Success";

	public static final String FAILURE_MESSAGE = "Failure";

	public static final String EMAIL_EXIST = "Email Exist";

	public static final String EMPLOYEE_STATUS = "Employee INACTIVE";

	public static final String ORGIN = "http://localhost:3000";

	public static final String SUCCESS_MESSAGE_EMPLOYEE_ADDED = "Employee Added";

	public static final String FAILURE_MESSAGE_EMPLOYEE_EXISTS = "Employee with email %s already exists";

	public static final String ERROR_PROCESSING_EMPLOYEE = "Error occurred while adding employee";

	public static final String NO_EMPLOYEES_FOUND = "No employees found in the system.";

	public static final String INVALID_BUSINESS_UNIT_HEAD_ID = "Invalid Business Unit Head ID: %s";

	public static final String NO_EMPLOYEES_FOUND_BU_HEAD = "No employees found for Business Unit Head ID: %s";

	public static final String EMPLOYEE_NOT_FOUND = "Employee not found with ID: %s";

	public static final String LOGIN_CREDENTIALS_NULL = "Login credentials cannot be null.";

	public static final String NO_EMPLOYEE_EMAIL_ADDRESSES_FOUND = "No employee email addresses found in the system.";

	public static final String EMAIL_CANNOT_BE_NULL = "Email cannot be null or empty";

	public static final String ROLE_NAME_CANNOT_BE_NULL = "Role name cannot be null or empty";

	public static final String NO_EMPLOYEES_FOUND_FOR_ROLE = "No employees found for role: %s";

	public static final String NO_CLIENT_PARTNER_FOUND = "No Client Partners found for BU Head ID: %s";

	public static final String NO_RECRUITING_MANAGER_FOUND = "No Recruiting Managers found for Client Partner with BU Head ID: %s";

	public static final String NO_RECRUITERS_FOUND = "No recruiters found for Recruiting Manager ID: %s";

	public static final String INVALID_EMPLOYEE_ID = "Invalid Employee ID: %s";

	public static final String BULK_EMPLOYEE_CREATION_FAILED = "Failed to add bulk employees. Please try again.";

	public static final String EMPLOYEE_STATUS_NOT_FOUND = "Employee status not found for email: %s";

	public static final String NO_EMPLOYEES_FOUND_FOR_RECRUITER = "No employees found for recruiter with ID: %s.";

	public static final String ERROR_CHECKING_EMAIL = "An error occurred while checking the email existence: %s";

}
