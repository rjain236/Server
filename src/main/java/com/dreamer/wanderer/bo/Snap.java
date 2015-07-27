package com.dreamer.wanderer.bo;

import com.dreamer.wanderer.data.PersistableBean;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.threeten.bp.ZonedDateTime;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rjain236 on 25/7/15.
 */
public abstract class Snap<DO extends PersistableBean, ME extends Snap> implements CachePersistableBean,PhysicalPersistableBean {

    private ZonedDateTime timeStamp = ZonedDateTime.now();
    private Long id;

    //Need a default constructor in all the business objects
    protected Snap() throws NoSuchMethodException {
        super();
        System.out.println( this.getClass().getName() ) ;
        this.getClass().getConstructor();
    }

    public ZonedDateTime getTimeStamp(){
        return timeStamp;
    }

    public ME getUpdatedInstance() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        List<Object> updatedArgs = new ArrayList<Object>();
        boolean rebuild = false;
        Field[] fields = this.getClass().getDeclaredFields();
        List<Object> dependsOn = new ArrayList<Object>();
        int index = 0;
        for(Field f:fields){
            dependsOn.add(FieldUtils.readField(f, this, true));
            index++;
        }
        for(Object o:dependsOn){
            if(o instanceof CachePersistableBean){
                CachePersistableBean<?> co = (CachePersistableBean<?>)o;
                //TODO add query by id over here query by id will always return updated snap(check against timestamp on hazelcast)
                co = co.getUpdatedInstance();
                if(co.getTimeStamp().isAfter(timeStamp))rebuild = true;
                o = co;
            }
            updatedArgs.add(o);
        }
        if(!rebuild)return (ME)this;
        Snap updatedObj = this.getClass().getConstructor().newInstance();
        index = 0;
        for(Field f:fields){
            FieldUtils.writeField(f,updatedObj,updatedArgs.get(index),true);
            index ++;
        }
        //TODO Save this update object in the cache as well
        return (ME) updatedObj;
    }

}
