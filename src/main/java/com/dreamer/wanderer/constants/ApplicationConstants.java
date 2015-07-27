package com.dreamer.wanderer.constants;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by rjain236 on 26/7/15.
 */
public class ApplicationConstants {

    public static final String DATA_CACHE_PROPERTY_NAME = "DATA_CACHE";

    public static final Set<String> CACHE_TYPES = new HashSet<String>(){
        {
            add(DATA_CACHE_PROPERTY_NAME);
            Collections.unmodifiableSet(this);
        }
    };

}
