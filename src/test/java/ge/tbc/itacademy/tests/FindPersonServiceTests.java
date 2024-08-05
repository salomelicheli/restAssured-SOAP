package ge.tbc.itacademy.tests;

import ge.tbc.itacademy.data.util.Unmarshall;
import ge.tbc.itacademy.steps.personsearchsteps.FindPersonSteps;
import org.tempuri.FindPersonResponse;
import org.tempuri.Person;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static ge.tbc.itacademy.data.constants.PersonData.*;

public class FindPersonServiceTests {
    FindPersonSteps steps;
    @BeforeClass
    public void beforeClass() {
        steps = new FindPersonSteps();
    }

    @Test
    public void ValidatingPersonData() {
        String body = steps.creatingRequestBody();
        String responseBody = steps.getFindPersonResponse(body).then().extract()
                .response()
                .body().asString();
        FindPersonResponse response
                = Unmarshall.unmarshallResponse(responseBody, FindPersonResponse.class);
        Person person = response.getFindPersonResult();
        steps.getPersonsFullName(person)
                .validatingPresenceOfElement(person)
                .validatingPersonsName(PERSON_FIRST_NAME)
                .validatingPersonsLastName(PERSON_LAST_NAME)
                .validatingSSNValue(person, PERSON_SSN)
                .validatingSSNPattern(person)
                .validatingSSNCharacterNumbers(person, PERSON_SSN)
                .validatingPersonsFavColors(person, FAVORITE_COLORS)
                .validatingDateOfBirth(person, PERSONS_DATE_OF_BIRTH)
                .validatingColors(person)
                .validatingZipCodes(person);
    }
}
