package com.dreamer.wanderer.bo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by rjain236 on 28/7/15.
 */
public aspect ConstructorInterceptor {
    after(CachePersistableBean cachableBean): execution(CachePersistableBean+.new(..)) && this(cachableBean) {
        System.out.println(thisJoinPointStaticPart);
        List<Object> args = new ArrayList<Object>(Arrays.asList(thisJoinPoint.getArgs()));
        cachableBean.setArguments(args);
    }
}
