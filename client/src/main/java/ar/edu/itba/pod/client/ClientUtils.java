package ar.edu.itba.pod.client;

import ar.edu.itba.pod.client.queries.QueryConsumer;
import ar.edu.itba.pod.models.Complaint;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;

import static ar.edu.itba.pod.Globals.*;
import static ar.edu.itba.pod.client.Constants.*;

@SuppressWarnings("deprecation")
public class ClientUtils {

    private static final Logger logger = LoggerFactory.getLogger(ClientUtils.class);

    private static java.util.logging.Logger setupLogger(Path outputPath) throws IOException {
        java.util.logging.Logger customLogger = java.util.logging.Logger.getLogger(CUSTOM_LOGGER_NAME);
        customLogger.setUseParentHandlers(false);
        customLogger.setLevel(Level.ALL);

        Formatter formatter = new java.util.logging.SimpleFormatter() {
                @Override
                public String format(java.util.logging.LogRecord record) {
                    String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SSSS")
                            .format(new Date(record.getMillis()));
                    return String.format("%s [%s] %s%n", date, record.getLevel(), record.getMessage());
                }
        };
        customLogger.addHandler(new FileHandler(outputPath.toString(), true) {{
            setFormatter(formatter);
        }});
        customLogger.addHandler(new ConsoleHandler() {{
            setFormatter(formatter);
        }});
        return customLogger;
    }

    public static void run(String jobTrackerName, int queryNumber, QueryConsumer query) {
        logger.info("Client Starting ...");

        try {
            // Group Config
            GroupConfig groupConfig = new GroupConfig().setName(USERNAME).setPassword(PASSWORD);
            // Client Network Config
            ClientNetworkConfig clientNetworkConfig = new ClientNetworkConfig();
            clientNetworkConfig.setAddresses(ArgumentParser.getAddressesArg());
            // Client Config
            ClientConfig clientConfig = new ClientConfig().setGroupConfig(groupConfig).setNetworkConfig(clientNetworkConfig);
            // Node Client
            HazelcastInstance hazelcastInstance = HazelcastClient.newHazelcastClient(clientConfig);

            final String inPath = ArgumentParser.getStringArg(IN_PATH_ARG);
            final String outPath = ArgumentParser.getStringArg(OUT_PATH_ARG);
            final String city = ArgumentParser.getStringArg(CITY_ARG);
            final Path inputPath = Paths.get(inPath, String.format(SERVICE_REQUESTS_FILE_TEMPLATE, city));

            final Path outFile = Paths.get(outPath, String.format(TIME_FILE_TEMPLATE, queryNumber));
            java.util.logging.Logger customLogger = setupLogger(outFile);

            IMap<String, Complaint> complaintsMap = hazelcastInstance.getMap(COMPLAINTS_MAP_NAME);
            customLogger.info("Inicio de la lectura del archivo de entrada: " + inputPath);
            Map<String, Complaint> batchMap = new HashMap<>();

            AtomicInteger i = new AtomicInteger();
            CsvManager.readFile(inputPath,
                    s -> ComplaintMappers.mappers.get(city).apply(s),
                    c -> {
                        i.getAndIncrement();
                        batchMap.put(c.getId(), c);
                        if (i.get() % COMPLAINT_BATCH_SIZE == 0) {
                            complaintsMap.putAll(batchMap);
                            batchMap.clear();
                        }
                    }
            );
            if (!batchMap.isEmpty()) complaintsMap.putAll(batchMap);
            customLogger.info("Fin de la lectura del archivo de entrada: " + inputPath);

            JobTracker jobTracker = hazelcastInstance.getJobTracker(jobTrackerName);
            try (KeyValueSource<String, Complaint> keyValueSource = KeyValueSource.fromMap(complaintsMap)) {
                query.accept(jobTracker, keyValueSource, hazelcastInstance, customLogger);
            }

            // Deallocate resources
            complaintsMap.destroy();
            IMap<String, String> typeMap = hazelcastInstance.getMap(TYPES_MAP_NAME);
            if (typeMap != null) typeMap.destroy();

        } catch (IllegalArgumentException e) {
            logger.error("Invalid argument: {}", e.getMessage());
        } catch (IOException e) {
            logger.error("Error reading input file: {}", e.getMessage());
        } catch (IllegalStateException e) {
            logger.error("Error with Hazelcast client configuration: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("An unexpected error occurred: {}", e.getMessage(), e);
        } finally {
            HazelcastClient.shutdownAll();
        }
    }

    public static void loadTypes(HazelcastInstance hazelcastInstance) throws IOException {
        final String inPath = ArgumentParser.getStringArg(IN_PATH_ARG);
        final String city = ArgumentParser.getStringArg(CITY_ARG);
        final Path inputPath = Paths.get(inPath, String.format(SERVICE_TYPES_FILE_TEMPLATE, city));
        IMap<String, String> typeMap = hazelcastInstance.getMap(TYPES_MAP_NAME);
        CsvManager.readFile(inputPath,
                s -> s,
                ComplaintMappers.complaintTypeConsumers.get(city).apply(typeMap)
        );
    }

}
