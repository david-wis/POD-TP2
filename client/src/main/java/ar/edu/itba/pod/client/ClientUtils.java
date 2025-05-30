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
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.SortedMap;
import java.util.concurrent.ExecutionException;

public class ClientUtils {

    private static final Logger logger = LoggerFactory.getLogger(ClientUtils.class);

    public static void run(String jobName) {
        logger.info("Client Starting ...");

        try {
            // Group Config
            GroupConfig groupConfig = new GroupConfig().setName("l12345").setPassword("l12345-pass");

            // Client Network Config
            ClientNetworkConfig clientNetworkConfig = new ClientNetworkConfig();

            // TODO: Change to set addresses
            clientNetworkConfig.addAddress("127.0.0.1");

            // Client Config
            ClientConfig clientConfig = new ClientConfig().setGroupConfig(groupConfig).setNetworkConfig(clientNetworkConfig);

            // Node Client
            HazelcastInstance hazelcastInstance = HazelcastClient.newHazelcastClient(clientConfig);

            JobTracker jobTracker = hazelcastInstance.getJobTracker(jobName);
//            final KeyValueSource<String, Complaint> keyValueSource = KeyValueSource
//
//            final ICompletableFuture<List<TotalComplaintsByTypeAgencyDTO>> future = job
//                    .mapper(new TotalComplaintsByTypeAgencyMapper())
//                    .reducer(new TotalComplaintsByTypeAgencyReducer())
//                    .submit(new TotalComplaintsByTypeAgencyCollator());




        } finally {
            HazelcastClient.shutdownAll();
        }
    }

}
