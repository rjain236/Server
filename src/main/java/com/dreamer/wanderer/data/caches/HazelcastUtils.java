package com.dreamer.wanderer.data.caches;

import com.hazelcast.core.HazelcastInstance;
import org.apache.log4j.Logger;

/**
 * Created by rjain236 on 27/7/15.
 */
public class HazelcastUtils {

    private static Logger logger = Logger.getLogger(HazelcastUtils.class);

    private static HazelcastUtils hazelcastUtils = new HazelcastUtils();

    public static HazelcastUtils getInstance() {
        return hazelcastUtils;
    }

    public HazelcastInstance getMainHazelcastInstance() {
        return CacheInstanceManager.getInstance().getHazelcastClientInstance(
                "DATA_CACHE");
    }
}
