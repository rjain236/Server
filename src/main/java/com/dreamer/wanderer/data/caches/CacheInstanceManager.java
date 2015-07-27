package com.dreamer.wanderer.data.caches;

/**
 * Created by rjain236 on 26/7/15.
 */

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.XmlClientConfigBuilder;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

public class CacheInstanceManager extends
        ConcurrentHashMap<String, HazelcastInstance> {

    private static final long serialVersionUID = 1L;

    private static Logger logger = Logger.getLogger(CacheInstanceManager.class);

    private static CacheInstanceManager cacheInstanceManager = null;
    private static Properties cacheManagerProperties = null;

    private CacheInstanceManager() {
        InputStream is = null;
        String propFileName = "";
        try {
            cacheManagerProperties = new Properties();
            propFileName = "/hazelcast-dev.properties";
            logger.info("picking config file location from " + propFileName);
            is = HazelcastCache.class.getResourceAsStream(propFileName);
            cacheManagerProperties.load(is);
        } catch (FileNotFoundException e) {
            logger.error("Properties file for hazelcast could not be loaded");
        } catch (Exception e) {
            logger.error("IO Exception while reading the cache properties file");
        }
        if (is != null) {
            try {
                is.close();
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
            is = null;
        }
        System.setProperty("java.net.preferIPv4Stack", "true");
    }

    public static CacheInstanceManager getInstance() {
        if (cacheInstanceManager == null) {
            cacheInstanceManager = new CacheInstanceManager();
        }
        return cacheInstanceManager;
    }

    public HazelcastInstance getHazelcastClientInstance(String cacheType) {

        HazelcastInstance clientInstance = cacheInstanceManager.get("CLIENT:"
                + cacheType);
        if (clientInstance == null) {
            String groupName = cacheManagerProperties.getProperty(cacheType
                    + ".GROUPNAME");
            String groupHash = cacheManagerProperties.getProperty(cacheType
                    + ".GROUPHASH");

            String connectionTimeout = cacheManagerProperties.getProperty(
                    cacheType + ".CLIENT.CON_TIMEOUT_MS", "5000");
            String connectionAttemptPeriod = cacheManagerProperties
                    .getProperty(cacheType + ".CLIENT.CON_ATT_PERIOD_MS",
                            "3000");
            String connectionAttemptLimit = cacheManagerProperties.getProperty(
                    cacheType + ".CLIENT.CON_ATT_LIMIT", "2");

            List<String> addresses = new ArrayList<>();
            addresses.addAll(CacheInstanceManager.getInstance().getIpAddresses(
                    cacheType));
            ClientConfig clientConfig = new ClientConfig();
            clientConfig.getNetworkConfig().setAddresses(addresses);
            clientConfig.getGroupConfig().setName(groupName);
            clientConfig.getGroupConfig().setPassword(groupHash);
            clientConfig.getNetworkConfig().setSmartRouting(true);
            clientConfig.getNetworkConfig().setConnectionTimeout(
                    Integer.parseInt(connectionTimeout));
            clientConfig.getNetworkConfig().setConnectionAttemptPeriod(
                    Integer.parseInt(connectionAttemptPeriod));
            clientConfig.getNetworkConfig().setConnectionAttemptLimit(
                    Integer.parseInt(connectionAttemptLimit));
            System.out.println(clientConfig);

            String configFileLoc = cacheManagerProperties.getProperty(cacheType
                    + ".CLIENT.XML", "");

            ClientConfig configXml = null;
            XmlClientConfigBuilder xmlClientConfigBuilder = null;
            try {
                System.out.println("Building configuration from"
                        + configFileLoc);
                xmlClientConfigBuilder = new XmlClientConfigBuilder(
                        configFileLoc);

                configXml = xmlClientConfigBuilder.build();
            } catch (Exception exception) {
                logger.error(exception);
            }

            clientInstance = HazelcastClient.newHazelcastClient(configXml);
            cacheInstanceManager.put("CLIENT:" + cacheType, clientInstance);
        }
        return clientInstance;

    }

    public HazelcastInstance getHazelcastInstance(String cacheType) {
        String file = cacheManagerProperties.getProperty(cacheType);
        String ipPort = cacheManagerProperties.getProperty(cacheType + ".PORT");
        String ipPortCount = cacheManagerProperties.getProperty(cacheType
                + ".PORTCOUNT");
        String groupName = cacheManagerProperties.getProperty(cacheType
                + ".GROUPNAME");
        String groupHash = cacheManagerProperties.getProperty(cacheType
                + ".GROUPHASH");
        List<String> ipInterfaces = getIpAddresses(cacheType);

        if (file == null || file.equals("")) {
            logger.error("No cache configuration specified for the given cache type ("
                    + cacheType + "). Cache cannot be created");
        }
        if (cacheInstanceManager.get(file) == null) {
            try {
                Config config = new Config();
                config.setInstanceName(cacheType);
                config.getGroupConfig().setName(groupName);
                config.getGroupConfig().setPassword(groupHash);
                config.getNetworkConfig().setPort(Integer.parseInt(ipPort));
                config.getNetworkConfig().setPortCount(
                        Integer.parseInt(ipPortCount));
                config.getNetworkConfig().getJoin().getMulticastConfig()
                        .setEnabled(false);
                config.getNetworkConfig().getJoin().getAwsConfig()
                        .setEnabled(false);
                config.getNetworkConfig().getJoin().getTcpIpConfig()
                        .setEnabled(true);
                Set<String> ipAddrSet = new HashSet<String>();
                for (String str : ipInterfaces) {
                    config.getNetworkConfig().getJoin().getTcpIpConfig()
                            .getMembers().add(str);
                    ipAddrSet.add(str.substring(0, str.indexOf(':')));
                }
                config.getNetworkConfig().getInterfaces().setEnabled(true);
                for (String str : ipAddrSet) {
                    config.getNetworkConfig().getInterfaces().addInterface(str);
                }
                HazelcastInstance instance = Hazelcast
                        .newHazelcastInstance(config);
                System.out.println(config);
                cacheInstanceManager.put(file, instance);
                logger.info("Successfully created the hazelcast instance for "
                        + file + " for the first time");

            } catch (Exception ex) {
                logger.error(ex);
                logger.error("Exception while starting hazelcast instance");
            }

        }
        return cacheInstanceManager.get(file);
    }

    public static String getProperty(String propertyName) {
        return cacheManagerProperties.getProperty(propertyName);
    }

    public List<String> getIpAddresses(String cacheType) {
        List<String> ipInterfaces = getIpArr(cacheManagerProperties
                .getProperty(cacheType + ".INTERFACES"));
        return ipInterfaces;
    }

    private static List<String> getIpArr(String str) {
        List<String> ipList = new ArrayList<String>();
        String[] topLevel = str.split(",");
        for (String topLevelStr : topLevel) {
            String[] midLevel = topLevelStr.split(":");
            String[] lowLevelIpRange = midLevel[0].split("-");
            String[] lowLevelPortRange = midLevel[1].split("-");
            lowLevelIpRange = getIpsFromRange(lowLevelIpRange);
            lowLevelPortRange = getPortsFromRange(lowLevelPortRange);

            for (String strIp : lowLevelIpRange) {
                for (String strPort : lowLevelPortRange) {
                    ipList.add(strIp.trim() + ":" + strPort.trim());
                }
            }
        }
        return ipList;
    }

    private static String[] getIpsFromRange(String[] lowLevelIpRange) {
        if (lowLevelIpRange == null || lowLevelIpRange.length < 2) {
            return lowLevelIpRange;
        } else {
            String prefix = lowLevelIpRange[0].substring(0,
                    1 + lowLevelIpRange[0].lastIndexOf('.'));
            int lowNumber = Integer.parseInt(lowLevelIpRange[0]
                    .substring(1 + lowLevelIpRange[0].lastIndexOf('.')));
            int highNumber = Integer
                    .parseInt(lowLevelIpRange[lowLevelIpRange.length - 1]);
            List<String> lowLevelPortRanngeUnwound = new ArrayList<String>();
            for (int i = lowNumber; i <= highNumber; i++) {
                lowLevelPortRanngeUnwound.add(prefix + i);
            }
            return lowLevelPortRanngeUnwound
                    .toArray(new String[lowLevelPortRanngeUnwound.size()]);
        }

    }

    private static String[] getPortsFromRange(String[] lowLevelPortRange) {
        if (lowLevelPortRange == null || lowLevelPortRange.length < 2) {
            return lowLevelPortRange;
        } else {
            List<String> lowLevelPortRanngeUnwound = new ArrayList<String>();
            int lowNumber = Integer.parseInt(lowLevelPortRange[0]);
            int highNumber = Integer
                    .parseInt(lowLevelPortRange[lowLevelPortRange.length - 1]);
            for (int i = lowNumber; i <= highNumber; i++) {
                lowLevelPortRanngeUnwound.add("" + i);
            }
            return lowLevelPortRanngeUnwound
                    .toArray(new String[lowLevelPortRanngeUnwound.size()]);

        }

    }
}

