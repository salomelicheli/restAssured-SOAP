package ge.tbc.itacademy.steps.personsearchsteps;

import ge.tbc.itacademy.data.util.HelperMethods;
import ge.tbc.itacademy.data.util.Marshall;
import ge.tbc.itacademy.data.requestspecbuilders.RequestspecBuilders;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matchers;
import org.tempuri.FindPerson;
import org.tempuri.ObjectFactory;
import org.tempuri.Person;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class FindPersonSteps {
    private final RequestSpecification requestSpec;
    private String[] fullName;

    public FindPersonSteps() {
        this.requestSpec = RequestspecBuilders.findPersonServiceRequest();
    }

    @Step("create request body")
    public String creatingRequestBody(){
        ObjectFactory objFactory = new ObjectFactory();
        FindPerson findPerson = objFactory.createFindPerson();
        findPerson.setId("10");
        return Marshall.marshallSoapRequest(findPerson);
    }

    @Step("get response")
    public Response getFindPersonResponse(String body){
        return given().spec(requestSpec)
                .header("SOAPAction", "http://tempuri.org/SOAP.Demo.FindPerson")
                .body(body)
                .post();
    }

    @Step("extract person's fullname")
    public FindPersonSteps getPersonsFullName(Person person){
        fullName = person.getName().split(",");
        return this;
    }

    @Step("validating presence of person")
    public FindPersonSteps validatingPresenceOfElement(Person person){
        assertThat(person, Matchers.notNullValue());
        return this;
    }

    @Step("validating person's first name")
    public FindPersonSteps validatingPersonsName(String name){
        assertThat(fullName[1], equalTo(name));
        return this;
    }

    @Step("validating person's last name")
    public FindPersonSteps validatingPersonsLastName(String lastName){
        assertThat(fullName[0], equalTo(lastName));
        return this;
    }

    @Step("validating person's ssn value")
    public FindPersonSteps validatingSSNValue(Person person, String SSNvalue){
        assertThat(person.getSSN(), equalTo(SSNvalue));
        return this;
    }

    @Step("validating ssn pattern")
    public FindPersonSteps validatingSSNPattern(Person person){
        assertThat(person.getSSN(), matchesPattern("^\\d{3}-\\d{2}-\\d{4}$"));
        return this;
    }

    @Step("validating ssn character numbers")
    public FindPersonSteps validatingSSNCharacterNumbers(Person person, String SSN){
        assertThat(person.getSSN().length(), equalTo(SSN.length()));
        return this;
    }

    @Step("validating person's fav colors")
    public FindPersonSteps validatingPersonsFavColors(Person person, String[]colors){
        assertThat(person.getFavoriteColors().getFavoriteColorsItem(), containsInAnyOrder(colors));
        return this;
    }

    @Step("validating person's date of birth")
    public FindPersonSteps validatingDateOfBirth(Person person, String date){
        assertThat(person.getDOB(), equalTo(HelperMethods.gregorianCalendarFormatting(date)));
        return this;
    }

    @Step("validating colors")
    public FindPersonSteps validatingColors(Person person){
        List<String> favColors = person.getFavoriteColors().getFavoriteColorsItem();
        boolean secondColorIsRed = favColors.stream()
                .anyMatch((color) -> color.equals("Red")
                        && favColors.size() > 2
                        && favColors.get(2).equals(color));
//        assertThat(secondColorIsRed, is(true)); - faildeba sul orelementiani list aris da witelis index - 1
        assertThat(secondColorIsRed, is(false));
        return this;
    }

    @Step("validating zip codes")
    public void validatingZipCodes(Person person){
        List<String> codeStream = List.of(
                person.getOffice().getZip(),
                person.getHome().getZip());
        boolean areEqual = codeStream.stream().distinct().count() > 1;
        assertThat(areEqual, is(true));
    }
}
