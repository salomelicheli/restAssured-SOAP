package ge.tbc.itacademy.steps.employeeservicesteps;

import com.example.springboot.soap.interfaces.AddEmployeeRequest;
import com.example.springboot.soap.interfaces.EmployeeInfo;
import com.example.springboot.soap.interfaces.ObjectFactory;
import ge.tbc.itacademy.data.requestspecbuilders.RequestspecBuilders;
import ge.tbc.itacademy.data.util.Marshall;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static ge.tbc.itacademy.data.constants.ConstantData.ADD_ACTION;
import static io.restassured.RestAssured.given;

public class AddEmployee {
    private AddEmployeeRequest addEmployee;
    private EmployeeInfo employee;
    private final RequestSpecification requestSpec;

    public AddEmployee() {
        requestSpec = RequestspecBuilders.employeeServiceRequest();
    }

    public EmployeeInfo getEmployee() {
        return employee;
    }

    @Step("step to create request body")
    public AddEmployee createAddRequest() throws DatatypeConfigurationException {
        ObjectFactory objectFactory = new ObjectFactory();
        LocalDate today = LocalDate.of(2001, 5, 16);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = today.format(formatter);
        XMLGregorianCalendar xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(formattedDate);
        addEmployee = objectFactory.createAddEmployeeRequest();
        employee = objectFactory.createEmployeeInfo();
        employee.withEmployeeId(100)
                .withAddress("Tbilisi")
                .withName("name")
                .withDepartment("departamenti")
                .withEmail("email@gmail.com")
                .withBirthDate(xmlCalendar)
                .withSalary(BigDecimal.valueOf(2000.00))
                .withPhone("212334542");

        return this;
    }

    @Step("step to set employee info")
    public AddEmployee setEmployeeInfo() {
        addEmployee.setEmployeeInfo(employee);
        return this;
    }

    @Step("step to send an add request")
    public Response sendAddRequest() {
        String body = Marshall.marshallSoapRequest(addEmployee);
        return given().spec(requestSpec)
                .header("SOAPAction", ADD_ACTION)
                .body(body)
                .post();
    }
}
