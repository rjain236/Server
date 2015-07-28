package com.dreamer.wanderer.bo;

import org.threeten.bp.ZonedDateTime;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Created by rjain236 on 28/7/15.
 */
public interface CachePersistableBean<T extends CachePersistableBean> extends Serializable{

    ZonedDateTime getTimeStamp();

    T getUpdatedInstance() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException;

}
