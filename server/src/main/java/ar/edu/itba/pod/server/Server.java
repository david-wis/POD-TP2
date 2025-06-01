package ar.edu.itba.pod.server;

import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
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

        // Network Config
        MulticastConfig multicastConfig = new MulticastConfig();

        JoinConfig joinConfig = new JoinConfig().setMulticastConfig(multicastConfig);

        InterfacesConfig interfacesConfig = new InterfacesConfig()
                .setInterfaces(getInterfacesArg()).setEnabled(true);

        NetworkConfig networkConfig = new NetworkConfig().setInterfaces(interfacesConfig).setJoin(joinConfig);

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


    public static Collection<String> getInterfacesArg() {
        String arg = System.getProperty("interfaces");

        if (arg == null || arg.isEmpty()) {
            logger.warn("No interfaces provided, using default: 127.0.0.*");
            return Collections.singletonList("127.0.0.*");
        }
        logger.info("Using interfaces provided: {}", arg);
        return List.of(arg.split(";"));
    }

}
