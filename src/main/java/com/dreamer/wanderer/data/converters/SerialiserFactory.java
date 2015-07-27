package com.dreamer.wanderer.data.converters;

import com.dreamer.wanderer.data.PersistableBean;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rjain236 on 26/7/15.
 */
@Service("SerialiserFactory")
public class SerialiserFactory {

    private Map<Class<?>,Serialiser> serialiserRegistry = null;

    private @Autowired
    ListableBeanFactory beanFactory;

    public Serialiser<?,?> getSerialiserInstance(Class<? extends PersistableBean> doClazz){
        if(serialiserRegistry==null){
            Map<String, Serialiser> serialisers = beanFactory.getBeansOfType(Serialiser.class);
            serialiserRegistry = new HashMap<Class<?>, Serialiser>();
            for(Serialiser s:serialisers.values()) serialiserRegistry.put(s.getDoClazz(), s);
        }
        return serialiserRegistry.get(doClazz);
    }
}
