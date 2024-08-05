package ge.tbc.itacademy.steps.employeeservicesteps;

import com.example.springboot.soap.interfaces.DeleteEmployeeRequest;
import com.example.springboot.soap.interfaces.DeleteEmployeeResponse;
import com.example.springboot.soap.interfaces.EmployeeInfo;
import com.example.springboot.soap.interfaces.ObjectFactory;
import ge.tbc.itacademy.data.requestspecbuilders.RequestspecBuilders;
import ge.tbc.itacademy.data.util.Unmarshall;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static ge.tbc.itacademy.data.constants.ConstantData.DELETE_ACTION;
import static io.restassured.RestAssured.given;

public class DeleteEmployee {
    private final RequestSpecification requestSpec;
    public DeleteEmployee() {
        requestSpec = RequestspecBuilders.employeeServiceRequest();
    }

    @Step("step to create delete request")
    public DeleteEmployeeRequest deleteEmployeeRequest(EmployeeInfo info) {
        ObjectFactory objectFactory = new ObjectFactory();
        DeleteEmployeeRequest delete = objectFactory.createDeleteEmployeeRequest();
        delete.setEmployeeId(info.getEmployeeId());
        return delete;
    }

    @Step("step to send delete request")
    public Response deleteEmployeeInfo(String xmlBody) {
        return given(requestSpec)
                .header("SOAPAction", DELETE_ACTION)
                .body(xmlBody)
                .post();
    }

    @Step("step to create get request")
    public DeleteEmployeeResponse deleteEmployeeByIdResponse(Response response) {
        return Unmarshall.unmarshallResponse(response.then()
                .extract()
                .body().asString(), DeleteEmployeeResponse.class);
    }
}
