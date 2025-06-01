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
import java.time.LocalDate;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;

import static ar.edu.itba.pod.client.Constants.*;

@SuppressWarnings("deprecation")
public class ClientUtils {

    private static final Logger logger = LoggerFactory.getLogger(ClientUtils.class);

    private static java.util.logging.Logger setupLogger(Path outputPath) throws IOException {
        java.util.logging.Logger logger = java.util.logging.Logger.getLogger(CUSTOM_LOGGER_NAME);
        logger.setUseParentHandlers(false);
        logger.setLevel(Level.ALL);

        Formatter formatter = new java.util.logging.SimpleFormatter() {
                @Override
                public String format(java.util.logging.LogRecord record) {
                    String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SSSS")
                            .format(new Date(record.getMillis()));
                    return String.format("%s [%s] %s%n", date, record.getLevel(), record.getMessage());
                }
        };
        logger.addHandler(new FileHandler(outputPath.toString(), true) {{
            setFormatter(formatter);
        }});
        logger.addHandler(new ConsoleHandler() {{
            setFormatter(formatter);
        }});
        return logger;
    }

    public static void run(String jobName, QueryConsumer query) {
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

            final Path outFile = Paths.get(outPath, String.format(TIME_FILE_TEMPLATE, 1)); // TODO: Pass number as argument
            java.util.logging.Logger customLogger = setupLogger(outFile);

            IMap<String, Complaint> complaintsMap = hazelcastInstance.getMap("complaints");
            customLogger.info("Inicio de la lectura del archivo de entrada: " + inputPath);
            CsvManager.readFile(inputPath,
                    s -> ComplaintMappers.mappers.get(city).apply(s),
                    c -> complaintsMap.put(c.getId(), c)
            );
            customLogger.info("Fin de la lectura del archivo de entrada: " + inputPath);

            JobTracker jobTracker = hazelcastInstance.getJobTracker(jobName);
            try (KeyValueSource<String, Complaint> keyValueSource = KeyValueSource.fromMap(complaintsMap)) {
                query.accept(jobTracker, keyValueSource, hazelcastInstance, customLogger);
            }
        } catch (IllegalArgumentException e) {
            logger.error("Invalid argument: {}", e.getMessage());
        } catch (IOException e) {
            logger.error("Error reading input file: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("An unexpected error occurred: {}", e.getMessage(), e);
        } finally {
            HazelcastClient.shutdownAll();
        }
    }

}
