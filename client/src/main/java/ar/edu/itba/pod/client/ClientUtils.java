package ar.edu.itba.pod.client;

import ar.edu.itba.pod.collators.TotalComplaintsByTypeAgencyCollator;
import ar.edu.itba.pod.mappers.TotalComplaintsByTypeAgencyMapper;
import ar.edu.itba.pod.models.Complaint;
import ar.edu.itba.pod.models.dto.TotalComplaintsByTypeAgencyDTO;
import ar.edu.itba.pod.reducers.TotalComplaintsByTypeAgencyReducer;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ICompletableFuture;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@SuppressWarnings("deprecation")
public class ClientUtils {

    private static final Logger logger = LoggerFactory.getLogger(ClientUtils.class);

    public static void run(String jobName, BiConsumer<JobTracker, KeyValueSource<String, Complaint>> query) throws IOException, ExecutionException, InterruptedException {
        logger.info("Client Starting ...");

        try {
            // Group Config
            GroupConfig groupConfig = new GroupConfig().setName("l12345").setPassword("l12345-pass");
            // Client Network Config
            ClientNetworkConfig clientNetworkConfig = new ClientNetworkConfig();
            clientNetworkConfig.addAddress("127.0.0.1"); // TODO: Change to set addresses
            // Client Config
            ClientConfig clientConfig = new ClientConfig().setGroupConfig(groupConfig).setNetworkConfig(clientNetworkConfig);
            // Node Client
            HazelcastInstance hazelcastInstance = HazelcastClient.newHazelcastClient(clientConfig);

            IMap<String, Complaint> complaintsMap = hazelcastInstance.getMap("complaints");
            // TODO: Add try catch for IOException and InterruptedException
            CsvReader.readFile("/home/david/Desktop/pod-tp2/archivos_pod/pod/testRequestsNYC.csv",
                    s -> {
                        Complaint.ComplaintBuilder builder = new Complaint.ComplaintBuilder();
                        try {
                            builder.setId(s[0])
                                   .setNeighborhood(s[6])
                                   .setLongitude(Float.parseFloat(s[7]))
                                   .setLatitude(Float.parseFloat(s[8]))
                                   .setDate(new SimpleDateFormat("yyyy-MM-dd").parse(s[1]))
                                   .setStreet(s[4])
                                   .setType(s[3])
                                   .setAgency(s[2]);
                        } catch (ParseException p) {
                            logger.error("Error parsing date: {}", s[1], p);
                        }
                        return builder.build();
                    }, c -> complaintsMap.put(c.getId(), c));

            JobTracker jobTracker = hazelcastInstance.getJobTracker(jobName);
            try (KeyValueSource<String, Complaint> keyValueSource = KeyValueSource.fromMap(complaintsMap)) {
                query.accept(jobTracker, keyValueSource);
            }
        } finally {
            HazelcastClient.shutdownAll();
        }
    }

}
