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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

@SuppressWarnings("deprecation")
public class ClientUtils {

    private static final Logger logger = LoggerFactory.getLogger(ClientUtils.class);

    public static void run(String jobName, QueryConsumer query) throws IOException, ExecutionException, InterruptedException {
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
            CsvReader.readFile("/Users/abrilvilamowski/Desktop/POD-TP2/archivos_testing/hola.csv",
                    s -> {
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
                        Complaint c = builder.build();
                        return c;
//                        return builder.build();
                    }, c -> complaintsMap.put(c.getId(), c));

            JobTracker jobTracker = hazelcastInstance.getJobTracker(jobName);
            try (KeyValueSource<String, Complaint> keyValueSource = KeyValueSource.fromMap(complaintsMap)) {
                query.accept(jobTracker, keyValueSource, hazelcastInstance);
            }
        } finally {
            HazelcastClient.shutdownAll();
        }
    }

}
