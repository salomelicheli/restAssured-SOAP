package ge.tbc.itacademy.data.util;

import javax.xml.namespace.NamespaceContext;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class EmployeeNamespaceContext implements NamespaceContext {
    private final Map<String, String> namespaces = new HashMap<>();

    public EmployeeNamespaceContext() {
        namespaces.put("ns2", "http://interfaces.soap.springboot.example.com");
        namespaces.put("soapenv", "http://schemas.xmlsoap.org/soap/envelope/");
    }

    @Override
    public String getNamespaceURI(String prefix) {
        return namespaces.get(prefix);
    }

    @Override
    public String getPrefix(String namespaceURI) {
        for (Map.Entry<String, String> entry : namespaces.entrySet()) {
            if (entry.getValue().equals(namespaceURI)) {
                return entry.getKey();
            }
        }
        return null;
    }

    @Override
    public Iterator<String> getPrefixes(String namespaceURI) {
        return namespaces.keySet().stream().filter(prefix -> namespaces.get(prefix).equals(namespaceURI))
                .iterator();
    }
}
