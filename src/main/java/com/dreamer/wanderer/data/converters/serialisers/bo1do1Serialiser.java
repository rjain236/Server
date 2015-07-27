package com.dreamer.wanderer.data.converters.serialisers;

import com.dreamer.wanderer.bo.beandefinitions.bo1;
import com.dreamer.wanderer.data.converters.Serialiser;
import com.dreamer.wanderer.data.dataobjects.do1;
import org.springframework.stereotype.Service;

/**
 * Created by rjain236 on 26/7/15.
 */
@Service("bo1do1Serialiser")
public class bo1do1Serialiser extends Serialiser<bo1,do1> {

    @Override
    public Class<?> getBoClazz() {
        return bo1.class;
    }

    @Override
    public Class<?> getDoClazz() {
        return do1.class;
    }

    @Override
    public bo1 postProcessing(bo1 bean){
        return bean;
    }
}
