package ge.tbc.itacademy.steps.employeeservicesteps;

import com.example.springboot.soap.interfaces.EmployeeInfo;
import com.example.springboot.soap.interfaces.ObjectFactory;
import com.example.springboot.soap.interfaces.UpdateEmployeeRequest;
import ge.tbc.itacademy.data.requestspecbuilders.RequestspecBuilders;
import io.qameta.allure.Step;
import io.restassured.specification.RequestSpecification;

import static ge.tbc.itacademy.data.constants.ConstantData.UPDATE_ACTION;
import static io.restassured.RestAssured.given;

public class UpdateEmployee {
    private final RequestSpecification requestSpec;
    public UpdateEmployee() {
        requestSpec = RequestspecBuilders.employeeServiceRequest();
    }
    @Step("step to create an update request")
    public UpdateEmployeeRequest updateEmployeeRequest(EmployeeInfo info) {
        ObjectFactory objectFactory = new ObjectFactory();
        UpdateEmployeeRequest update = objectFactory.createUpdateEmployeeRequest();
        info.withEmail("someEmail@gmail.com")
                .withAddress("Gldani");
        update.setEmployeeInfo(info);
        return update;
    }

    @Step("step to send a request to update employee info")
    public void updateEmployeeInfo(String xmlBody) {
        given(requestSpec)
                .header("SOAPAction", UPDATE_ACTION)
                .body(xmlBody)
                .post()
                .then().statusCode(200);
    }
}
