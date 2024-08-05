package ge.tbc.itacademy.data.util;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class HelperMethods {
    public static XMLGregorianCalendar gregorianCalendarFormatting(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);
        try {
            return DatatypeFactory.newInstance()
                    .newXMLGregorianCalendar(localDate.toString());
        } catch (DatatypeConfigurationException ignored) {

        }
        return null;
    }
}
