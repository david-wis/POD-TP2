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
import java.util.List;
import java.util.concurrent.ExecutionException;

@SuppressWarnings("deprecation")
public class ClientUtils {

    private static final Logger logger = LoggerFactory.getLogger(ClientUtils.class);

    public static void run(String jobName) throws IOException, ExecutionException, InterruptedException {
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
                    s -> new Complaint(s[0], s[2], s[3]), c -> complaintsMap.put(c.getId(), c));

            JobTracker jobTracker = hazelcastInstance.getJobTracker(jobName);
            try (KeyValueSource<String, Complaint> keyValueSource = KeyValueSource.fromMap(complaintsMap)) {
                ICompletableFuture<List<TotalComplaintsByTypeAgencyDTO>> futureResponse = jobTracker.newJob(keyValueSource)
                        .mapper(new TotalComplaintsByTypeAgencyMapper())
                        .reducer(new TotalComplaintsByTypeAgencyReducer())
                        .submit(new TotalComplaintsByTypeAgencyCollator());

                List<TotalComplaintsByTypeAgencyDTO> result = futureResponse.get();
                result.forEach(totalComplaintsByTypeAgencyDTO -> {
                    logger.info("Total: {}, Type: {}, Agency: {}",
                            totalComplaintsByTypeAgencyDTO.getTotal(),
                            totalComplaintsByTypeAgencyDTO.getType(),
                            totalComplaintsByTypeAgencyDTO.getAgency());
                });
            }
        } finally {
            HazelcastClient.shutdownAll();
        }
    }

}
