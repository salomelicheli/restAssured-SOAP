package ge.tbc.itacademy.data.requestspecbuilders;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

import static ge.tbc.itacademy.data.constants.ConstantData.EMPLOYEE_URI;
import static ge.tbc.itacademy.data.constants.ConstantData.FIND_PERSON_URI;

public class RequestspecBuilders {
    public static RequestSpecification employeeServiceRequest(){
        return new RequestSpecBuilder()
                .setBaseUri(EMPLOYEE_URI)
                .addHeader("Content-Type", "text/xml; charset=utf-8")
                .build()
                .filter(new AllureRestAssured());
    }

    public static RequestSpecification findPersonServiceRequest(){
        return new RequestSpecBuilder()
                .setBaseUri(FIND_PERSON_URI)
                .addHeader("Content-Type", "text/xml; charset=utf-8")
                .build()
                .filter(new AllureRestAssured());
    }
}
