package com.dreamer.wanderer.data.converters;

import com.dreamer.wanderer.bo.Snap;
import com.dreamer.wanderer.data.ConcreteBean;
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

    private Map<DoBoKey,Serialiser> serialiserRegistry = null;

    private @Autowired
    ListableBeanFactory beanFactory;

    public Serialiser<? extends Snap,? extends ConcreteBean> getSerialiserInstance(Class<?> clazz){
        if(serialiserRegistry==null){
            Map<String, Serialiser> serialisers = beanFactory.getBeansOfType(Serialiser.class);
            serialiserRegistry = new HashMap<DoBoKey, Serialiser>();
            for(Serialiser s:serialisers.values()) serialiserRegistry.put(new DoBoKey(s.getDoClazz(),s.getBoClazz()), s);
        }
        return serialiserRegistry.get(clazz);
    }


    private class DoBoKey{

        private Class<?> doClazz;
        private Class<?> boClazz;

        public DoBoKey(Class<?> doClazz, Class<?> boClazz) {
            this.doClazz = doClazz;
            this.boClazz = boClazz;
        }

        @Override
        public boolean equals(Object o) {
            if(doClazz==null||boClazz==null||o==null)return false;
            if(o instanceof DoBoKey){
                DoBoKey other = (DoBoKey)o;
                if(other.boClazz==null||other.doClazz==null)return false;
                if(other.boClazz.equals(boClazz)&&other.doClazz.equals(doClazz))return true;
                return false;
            }
            if(doClazz.equals(o)||boClazz.equals(o))return true;
            return false;
        }

        @Override
        public int hashCode() {
            return 0;
        }
    }
}
