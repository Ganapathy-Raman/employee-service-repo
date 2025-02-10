package com.rts.tap.constants;

public class APIConstants {

	private APIConstants() {
		throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
	}

	public static final String BASE_URL = "/employee-service/tap";
	public static final String FRONT_END_URL = "http://localhost:3000";
	public static final String CROSS_ORIGIN_URL = "http://localhost:3000";
	public static final String ADD_EMPLOYEE_URL = "/createemployee";
	public static final String ADD_BULK_EMPLOYEE_URL = "/createbulkemployee";
	public static final String GETALL_EMPLOYEE_URL = "/getallemployee";
	public static final String GET_EMPLOYEES_BY_BUSINESS_UNIT_HEAD = "/getemployeesbybusinessunithead";
	public static final String UPDATE_EMPLOYEE_URL = "/updateemployee/{employeeId}";

	public static final String GET_EMPLOYEE_BY_ID = "/getEmployeeById/{employeeId}";
	public static final String GETALL_EMPLOYEE_EMAIL_URL = "/getAllEmployeeEmail";
	public static final String CHECK_EMPLOYEE_STATUS_URL = "/checkEmployeeStatus";
	public static final String UPDATE_EMPLOYEE_STATUS_URL = "/updateEmployeeStatus/{employeeId}";
	public static final String GET_EMPLOYEE_BY_EMAIL = "/checkemailexist/{email}";
	public static final String GET_EMPLOYEES_BY_RECRUITER = "/by-role-name/{roleName}";
	public static final String GET_CLIENTPARTNER_BY_BUSINESSUNITHEAD = "/getclientpartnerbybusinessunithead/{buHeadId}";
	public static final String GET_CLIENTPARTNER_BU = "/getClientPartner/bu/{buId}";

	public static final String GET_ALL_EMPLOYEE_BY_ROLE = "/getEmployeesByRoles";

}
