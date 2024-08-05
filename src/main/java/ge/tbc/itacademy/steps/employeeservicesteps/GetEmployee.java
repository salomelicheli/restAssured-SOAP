package ge.tbc.itacademy.steps.employeeservicesteps;

import com.example.springboot.soap.interfaces.EmployeeInfo;
import com.example.springboot.soap.interfaces.GetEmployeeByIdRequest;
import com.example.springboot.soap.interfaces.GetEmployeeByIdResponse;
import com.example.springboot.soap.interfaces.ObjectFactory;
import ge.tbc.itacademy.data.requestspecbuilders.RequestspecBuilders;
import ge.tbc.itacademy.data.util.Unmarshall;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import jakarta.xml.ws.soap.SOAPFaultException;

import static ge.tbc.itacademy.data.constants.ConstantData.GET_ACTION;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class GetEmployee {
    private final RequestSpecification requestSpec;
    public GetEmployee() {
        requestSpec = RequestspecBuilders.employeeServiceRequest();
    }
    @Step("step to create get request")
    public GetEmployeeByIdRequest getEmployeeByIdRequest(EmployeeInfo employee) {
        ObjectFactory objectFactory = new ObjectFactory();
        GetEmployeeByIdRequest getEmployeeByIdRequest = objectFactory.createGetEmployeeByIdRequest();
        getEmployeeByIdRequest.setEmployeeId(employee.getEmployeeId());
        return getEmployeeByIdRequest;
    }
    @Step("step to get employee by id")
    public Response getEmployeeById(String xmlBody) {
        return given(requestSpec)
                .header("SOAPAction", GET_ACTION)
                .body(xmlBody)
                .post();
    }
    @Step("step to create get request")
    public GetEmployeeByIdResponse getEmployeeByIdResponse(Response response) {
        GetEmployeeByIdResponse getEmployeeByIdResponse = null;
        try {
            getEmployeeByIdResponse = Unmarshall.unmarshallResponse(response.then()
                    .extract().body().asString(), GetEmployeeByIdResponse.class);
        }catch(SOAPFaultException e){
            System.out.println(e.getMessage());
        }
        return getEmployeeByIdResponse;
    }

    @Step("validating employee id")
    public GetEmployee validatingEmployeeId(GetEmployeeByIdResponse response, EmployeeInfo employee){
        assertThat(employee.getEmployeeId(), equalTo(response.getEmployeeInfo().getEmployeeId()));
        return this;
    }
    @Step("validating employee name")
    public GetEmployee validatingEmployeeName(GetEmployeeByIdResponse response, EmployeeInfo employee){
        assertThat(employee.getName(), equalTo(response.getEmployeeInfo().getName()));
        return this;
    }
    @Step("validating employee department")
    public GetEmployee validateEmployeeDepartment(GetEmployeeByIdResponse response, EmployeeInfo employee){
        assertThat(employee.getEmployeeId(), equalTo(response.getEmployeeInfo().getEmployeeId()));
        return this;
    }
    @Step("validating employee address")
    public GetEmployee validatingEmployeeAddress(GetEmployeeByIdResponse response, EmployeeInfo employee){
        assertThat(employee.getAddress(), equalTo(response.getEmployeeInfo().getAddress()));
        return this;
    }
    @Step("validating employee phone number")
    public GetEmployee validatingEmployeePhone(GetEmployeeByIdResponse response, EmployeeInfo employee){
        assertThat(employee.getPhone(), equalTo(response.getEmployeeInfo().getPhone()));
        return this;
    }
    @Step("validating employee email")
    public GetEmployee validatingEmployeeEmail(GetEmployeeByIdResponse response, EmployeeInfo employee){
        assertThat(employee.getEmail(), equalTo(response.getEmployeeInfo().getEmail()));
        return this;
    }
    @Step("validating employee salary")
    public GetEmployee validatingEmployeeSalary(GetEmployeeByIdResponse response, EmployeeInfo employee){
        assertThat(String.format("%.2f",employee.getSalary()), equalTo(response.getEmployeeInfo().getSalary().toString()));
        return this;
    }
    @Step("validating employee birth date")
    public void validatingEmployeeBirthdate(GetEmployeeByIdResponse response, EmployeeInfo employee){
        assertThat(employee.getBirthDate(), equalTo(response.getEmployeeInfo().getBirthDate()));
    }
}
