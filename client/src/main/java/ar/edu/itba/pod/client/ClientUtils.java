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
import java.time.YearMonth;
import java.nio.file.Path;
import java.nio.file.Paths;

import static ar.edu.itba.pod.client.Constants.*;

@SuppressWarnings("deprecation")
public class ClientUtils {

    private static final Logger logger = LoggerFactory.getLogger(ClientUtils.class);

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
            final String city = ArgumentParser.getStringArg(CITY_ARG);
            final Path inputPath = Paths.get(inPath, String.format(SERVICE_REQUESTS_FILE_TEMPLATE, city));

            IMap<String, Complaint> complaintsMap = hazelcastInstance.getMap("complaints");
            CsvManager.readFile(inputPath, s -> {
                        Complaint.ComplaintBuilder builder = new Complaint.ComplaintBuilder();
                        builder.setId(s[0])
                                .setNeighborhood(s[6])
                                .setLongitude(Float.parseFloat(s[7]))
                                .setLatitude(Float.parseFloat(s[8]))
                                .setDate(YearMonth.parse(s[1].substring(0, 7)))
                                .setStreet(s[4])
                                .setType(s[3])
                                .setOpen(!s[5].equals("Closed"))
                                .setAgency(s[2]);
                        return builder.build();
            }, c -> complaintsMap.put(c.getId(), c));

            JobTracker jobTracker = hazelcastInstance.getJobTracker(jobName);
            try (KeyValueSource<String, Complaint> keyValueSource = KeyValueSource.fromMap(complaintsMap)) {
                query.accept(jobTracker, keyValueSource, hazelcastInstance);
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
