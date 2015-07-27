package com.dreamer.wanderer.bo;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.threeten.bp.ZonedDateTime;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rjain236 on 27/7/15.
 */
public abstract class Derived<T extends Derived> implements CachePersistableBean<T>{

    private ZonedDateTime timeStamp = ZonedDateTime.now();

    //Populate this list to maintain dependency
    private List<Object> dependsOn = new ArrayList<Object>();

    public ZonedDateTime getTimeStamp(){
        return timeStamp;
    }

    public void setArguments(List<Object> args){
        this.dependsOn = args;
    }

    public T getUpdatedInstance() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        List<Object> updatedArgs = new ArrayList<Object>();
        boolean rebuild = false;
        for(Object o:dependsOn){
            if(o instanceof CachePersistableBean){
                CachePersistableBean<?> co = (CachePersistableBean<?>)o;
                co = co.getUpdatedInstance();
                if(co.getTimeStamp().isAfter(timeStamp))rebuild = true;
                o = co;
            }
            updatedArgs.add(o);
        }
        if(!rebuild)return (T)this;
        Class[] clazzList = new Class[updatedArgs.size()];
        int index = 0;
        for(Object o:updatedArgs){
            clazzList[index] = o.getClass();
            index ++;
        }
        //TODO Save this update object in the cache as well
        return (T) this.getClass().getConstructor(clazzList).newInstance(updatedArgs);
    }


}
