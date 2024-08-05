package ge.tbc.itacademy.data.util;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.ws.soap.SOAPFaultException;
import org.w3c.dom.Document;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Unmarshall {
    public static <T> T unmarshallResponse(String response, Class<T> object) {
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(response.getBytes(StandardCharsets.UTF_8));
            SOAPMessage message = MessageFactory.newInstance().createMessage(null, byteArrayInputStream);
            SOAPFaultException ex = message.getSOAPBody().hasFault() ? new SOAPFaultException(message.getSOAPBody()
                    .getFault()) : null;
            if (ex != null) {
                throw ex;
            }
            Document document = message.getSOAPBody().extractContentAsDocument();
            Unmarshaller unmarshaller = JAXBContext.newInstance(object).createUnmarshaller();
            Object unmarshal = unmarshaller.unmarshal(document);
            return object.cast(unmarshal);
        } catch (SOAPException | JAXBException | IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
