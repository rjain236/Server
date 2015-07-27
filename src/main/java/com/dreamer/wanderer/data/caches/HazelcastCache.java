package com.dreamer.wanderer.data.caches;

/**
 * Created by rjain236 on 26/7/15.
 */

import java.io.IOException;

import com.dreamer.wanderer.constants.ApplicationConstants;
import com.hazelcast.core.HazelcastInstance;

public class HazelcastCache {

    public static void initialiseHazelcastCache(String serverType) {
        if (serverType == null || serverType.equals("")
                || !ApplicationConstants.CACHE_TYPES.contains(serverType)) {
            for (String serverTypeTemp : ApplicationConstants.CACHE_TYPES) {
                CacheInstanceManager.getInstance().getHazelcastInstance(
                        serverTypeTemp);
            }

        } else {
            System.out.println("Starting specific instance of cache type "
                    + serverType);

            CacheInstanceManager.getInstance().getHazelcastInstance(serverType);
        }
        System.out.println("Hazelcast instances are up and running");
    }

    public static void main(String[] args) throws IOException {
        System.setProperty("java.net.preferIPv4Stack", "true");
        String serverType = "";
        if (args.length > 0) {
            serverType = args[0];
            if (serverType != null && ApplicationConstants.CACHE_TYPES.contains(serverType)) {
            } else {
                if (serverType != null) {
                    System.err.println("Unrecognized server type" + serverType
                            + " should be one of " + ApplicationConstants.CACHE_TYPES.toString());
                }
                serverType = "";
            }
        }
        initialiseHazelcastCache(serverType);
    }

    protected void finalize() throws Throwable {
        HazelcastInstance fmconvegeInstance = CacheInstanceManager
                .getInstance().getHazelcastInstance(
                        ApplicationConstants.DATA_CACHE_PROPERTY_NAME);
        if (fmconvegeInstance != null)
            fmconvegeInstance.shutdown();
    }
}

