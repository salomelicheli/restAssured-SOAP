package ge.tbc.itacademy.tests;

import com.example.springboot.soap.interfaces.*;
import ge.tbc.itacademy.steps.employeeservicesteps.AddEmployee;
import ge.tbc.itacademy.steps.employeeservicesteps.DeleteEmployee;
import ge.tbc.itacademy.steps.employeeservicesteps.GetEmployee;
import ge.tbc.itacademy.steps.employeeservicesteps.UpdateEmployee;
import ge.tbc.itacademy.data.util.Marshall;
import ge.tbc.itacademy.data.util.Unmarshall;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.xml.datatype.DatatypeConfigurationException;

import static ge.tbc.itacademy.data.constants.ConstantData.SUCCESSFUL_DELETION_MESSAGE;
import static ge.tbc.itacademy.data.constants.ConstantData.SUCCESSFUL_OPERATION_MESSAGE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class EmployeeServiceTests {
    AddEmployee steps;
    UpdateEmployee updateSteps;
    DeleteEmployee deleteSteps;
    GetEmployee getSteps;
    @BeforeClass
    public void beforeClass() {
        steps = new AddEmployee();
        updateSteps = new UpdateEmployee();
        deleteSteps = new DeleteEmployee();
        getSteps = new GetEmployee();
    }

    @Test
    @Feature("added employee data validations")
    public void AddNewEmployee() throws DatatypeConfigurationException {
        String resp= steps.createAddRequest()
                .setEmployeeInfo()
                .sendAddRequest().then()
                .extract()
                .response().asString();
        AddEmployeeResponse employeeResponse =
                Unmarshall.unmarshallResponse(resp, AddEmployeeResponse.class );
        assertThat(employeeResponse.getServiceStatus().getMessage(),
                equalTo(SUCCESSFUL_OPERATION_MESSAGE));
    }

    @Test(dependsOnMethods = {"AddNewEmployee"}, description = "getting added employee by id")
    public void getEmployee(){
        var employee = steps.getEmployee();
        GetEmployeeByIdRequest request =
                getSteps.getEmployeeByIdRequest(employee);
        String body = Marshall.marshallSoapRequest(request);
        Response response = getSteps.getEmployeeById(body);
        GetEmployeeByIdResponse getEmployee =
                getSteps.getEmployeeByIdResponse(response);
        getSteps.validatingEmployeeName(getEmployee, employee)
                .validatingEmployeeId(getEmployee, employee)
                .validatingEmployeePhone(getEmployee, employee)
                .validatingEmployeeAddress(getEmployee, employee)
                .validateEmployeeDepartment(getEmployee, employee)
                .validatingEmployeeEmail(getEmployee, employee)
                .validatingEmployeeSalary(getEmployee, employee)
                .validatingEmployeeBirthdate(getEmployee, employee);
    }

    @Test(dependsOnMethods = {"AddNewEmployee", "getEmployee"}, description = "updating added employee")
    @Feature("updated employee data validations")
    public void UpdateEmployee(){
        UpdateEmployeeRequest update =
                updateSteps.updateEmployeeRequest(steps.getEmployee());
        String body = Marshall.marshallSoapRequest(update);
        GetEmployeeByIdRequest getRequest =
                getSteps.getEmployeeByIdRequest(steps.getEmployee());
        String xmlBody = Marshall.marshallSoapRequest(getRequest);
        updateSteps.updateEmployeeInfo(body);
        Response response = getSteps.getEmployeeById(xmlBody);
        GetEmployeeByIdResponse getEmployee =
                getSteps.getEmployeeByIdResponse(response);
        getSteps.validatingEmployeeAddress(getEmployee, steps.getEmployee())
                .validatingEmployeeEmail(getEmployee, steps.getEmployee());
    }

    @Test(dependsOnMethods = {"AddNewEmployee", "getEmployee", "UpdateEmployee"}, description = "deleting added employee")
    public void DeleteAddedEmployee(){
        DeleteEmployeeRequest deleteRequest =
                deleteSteps.deleteEmployeeRequest(steps.getEmployee());
        String body = Marshall.marshallSoapRequest(deleteRequest);
        GetEmployeeByIdRequest getEmployee =
                getSteps.getEmployeeByIdRequest(steps.getEmployee());
        String xmlBody = Marshall.marshallSoapRequest(getEmployee);
        Response deleteResponse = deleteSteps.deleteEmployeeInfo(body);
        DeleteEmployeeResponse deleteEmployeeResponse
                = deleteSteps.deleteEmployeeByIdResponse(deleteResponse);
        assertThat(deleteEmployeeResponse.getServiceStatus().getMessage(), equalTo(SUCCESSFUL_DELETION_MESSAGE));
        Response response = getSteps.getEmployeeById(xmlBody);
        response.then().statusCode(500);
        getSteps.getEmployeeByIdResponse(response);
    }
}
