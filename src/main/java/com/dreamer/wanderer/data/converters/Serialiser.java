package com.dreamer.wanderer.data.converters;

import com.dreamer.wanderer.bo.Snap;
import com.dreamer.wanderer.data.ConcreteBean;
import com.dreamer.wanderer.data.annotation.HibernateBeanAnnotation;
import com.dreamer.wanderer.data.annotation.NeoBeanAnnotation;
import com.dreamer.wanderer.data.annotation.NormalAnnotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by rjain236 on 25/7/15.
 */
public abstract class Serialiser<BO extends Snap,DO extends ConcreteBean> {

    public abstract Class<?> getBoClazz();

    public abstract Class<?> getDoClazz();

    public BO getSnap(DO dataObject) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        Class<?> boClazz = getBoClazz();
        Constructor<?> constructor = boClazz.getDeclaredConstructor(Object.class);
        constructor.setAccessible(true);
        Object boObject = constructor.newInstance();

        for(Field field  : dataObject.getClass().getDeclaredFields())
        {
            field.get(dataObject);
        }
        return null;
    }

    public DO serialise(BO snap){
        //TODO implement the conversion of bo to do
        return null;
    }

    protected BO postProcessing(BO bean){
        return bean;
    }

    private Object processAnnotation(Annotation[] annotations,Object value){
        for(int i = 0; i<annotations.length;i++){
            if(annotations[i] instanceof HibernateBeanAnnotation){
                HibernateBeanAnnotation annotation = (HibernateBeanAnnotation)annotations[i];
                Class<?> clazz = annotation.type();
                Long id = Long.class.cast(value);
                //Checking once if it is of correct type
                return clazz.cast(value);
            }
            if(annotations[i] instanceof NeoBeanAnnotation){
                NeoBeanAnnotation annotation = (NeoBeanAnnotation)annotations[i];
                Class<?> clazz = annotation.type();
                Long id = Long.class.cast(value);
            }
            if(annotations[i] instanceof NormalAnnotation){
                NormalAnnotation annotation = (NormalAnnotation)annotations[i];
                return value;
            }
        }
        return null;
    }
}
