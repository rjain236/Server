package com.dreamer.wanderer.data.converters;

import com.dreamer.wanderer.data.ConcreteBean;
import com.dreamer.wanderer.data.hibernate.beans.SchemaBasedBean;
import com.dreamer.wanderer.utils.compression.CompressionUtils;
import com.dreamer.wanderer.utils.marshaller.MarshallerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;

/**
 * Created by rjain236 on 26/7/15.
 */
@Service("SerialiserSchemaBasedBean")
public class SerialiserSchemaBasedBean {

    @Autowired
    private MarshallerUtils marshallerUtils;

    @Autowired
    private CompressionUtils compressionUtils;

    public Object getConcrete(SchemaBasedBean schema) throws ClassNotFoundException, JAXBException {
        Class<?> clazz = Class.forName(schema.getClazz());
        return marshallerUtils.unmarshall(marshallerUtils.json, new StreamSource(new StringReader(compressionUtils.decompress(schema.getSchemaData()))),clazz);
    }

    public <DO extends ConcreteBean> Object getAbstract(DO bean) throws JAXBException {
        String json = marshallerUtils.marshall(marshallerUtils.json,bean);
        String clazz = bean.getClass().getName();
        return new SchemaBasedBean(clazz,"",compressionUtils.compress(json));
    }
}
