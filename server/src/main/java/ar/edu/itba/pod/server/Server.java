package ar.edu.itba.pod.server;

import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

import static ar.edu.itba.pod.Globals.PASSWORD;
import static ar.edu.itba.pod.Globals.USERNAME;

public class Server {

    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    public static void main(String[] args) {
        logger.info(" Server Starting ...");

        // Config
        Config config = new Config();

        // Group Config
        GroupConfig groupConfig = new GroupConfig().setName(USERNAME).setPassword(PASSWORD);
        config.setGroupConfig(groupConfig);

        InterfacesConfig interfacesConfig = new InterfacesConfig()
                .setInterfaces(getListArg("interfaces")).setEnabled(true);

        NetworkConfig networkConfig = new NetworkConfig().setInterfaces(interfacesConfig);

        String publicAddress = System.getProperty("publicAddress");
        if (publicAddress != null)
            networkConfig.setPublicAddress(publicAddress);

        String mode = System.getProperty("mode", "multicast").toLowerCase();
        JoinConfig joinConfig = new JoinConfig();

        if (mode.equals("tcp")) {
            logger.info("Using TCP/IP cluster discovery");

            List<String> members = getListArg("members");
            logger.info("Members: {}", members);

            TcpIpConfig tcpIpConfig = new TcpIpConfig()
                    .setEnabled(true)
                    .setMembers(members);

            joinConfig.setTcpIpConfig(tcpIpConfig);
            joinConfig.getMulticastConfig().setEnabled(false);

        } else {
            logger.info("Using Multicast cluster discovery");

            MulticastConfig multicastConfig = new MulticastConfig().setEnabled(true);

            joinConfig.setMulticastConfig(multicastConfig);
            joinConfig.getTcpIpConfig().setEnabled(false);
        }

        networkConfig.setJoin(joinConfig);
        config.setNetworkConfig(networkConfig);


        // Opcional: Logger detallado
//        java.util.logging.Logger rootLogger = LogManager.getLogManager().getLogger("");
//        rootLogger.setLevel(Level.FINE);
//        for (Handler h : rootLogger.getHandlers()) {
//            h.setLevel(Level.FINE);
//        }

        // Start cluster
        Hazelcast.newHazelcastInstance(config);
    }


    public static List<String> getListArg(String name) {
        String arg = System.getProperty(name);

        if (arg == null || arg.isEmpty()) {
            logger.warn("No interfaces provided, using default: 127.0.0.*");
//            return Collections.singletonList("127.0.0.*");
            return Collections.singletonList("192.168.1.*");
        }
        logger.info("Using interfaces provided: {}", arg);
        return List.of(arg.split(";"));
    }

}
