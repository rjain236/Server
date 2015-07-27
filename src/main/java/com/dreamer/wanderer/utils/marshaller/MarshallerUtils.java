package com.dreamer.wanderer.utils.marshaller;

import org.eclipse.persistence.jaxb.MarshallerProperties;
import org.eclipse.persistence.jaxb.UnmarshallerProperties;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.StringWriter;

/**
 * Created by rjain236 on 26/7/15.
 */
@Service("MarshallerUtils")
public class MarshallerUtils {

    public String json = "application/json";

    public <T> String marshall(String type, T obj) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(obj.getClass());
        StringWriter sw = new StringWriter();
        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(MarshallerProperties.MEDIA_TYPE, type);
        marshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, true);
//        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(obj, sw);
        return sw.toString();
    }

    public <T> T unmarshall(String type, StreamSource input, Class<T> clazz) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(clazz);
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        unmarshaller.setProperty(UnmarshallerProperties.MEDIA_TYPE, type);
        unmarshaller.setProperty(UnmarshallerProperties.JSON_INCLUDE_ROOT, true);
        return unmarshaller.unmarshal(input, clazz).getValue();
    }
}
