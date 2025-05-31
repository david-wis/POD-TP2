package ar.edu.itba.pod.client.queries;

import ar.edu.itba.pod.client.ClientUtils;
import ar.edu.itba.pod.collators.TotalComplaintsByTypeAgencyCollator;
import ar.edu.itba.pod.mappers.TotalComplaintsByTypeAgencyMapper;
import ar.edu.itba.pod.models.dto.TotalComplaintsByTypeAgencyDTO;
import ar.edu.itba.pod.reducers.TotalComplaintsByTypeAgencyReducerFactory;
import com.hazelcast.core.ICompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static java.lang.System.exit;

public class Query1 {

    private static final Logger logger = LoggerFactory.getLogger(Query1.class);

    // TODO: Remove throws
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        ClientUtils.run("TotalComplaintsByTypeAgency", (jobTracker, keyValueSource, hazelcastInstance) -> {
            ICompletableFuture<List<TotalComplaintsByTypeAgencyDTO>> futureResponse = jobTracker.newJob(keyValueSource)
                    .mapper(new TotalComplaintsByTypeAgencyMapper())
                    .reducer(new TotalComplaintsByTypeAgencyReducerFactory())
                    .submit(new TotalComplaintsByTypeAgencyCollator());

            List<TotalComplaintsByTypeAgencyDTO> result = null;
            try {
                result = futureResponse.get();
            } catch (InterruptedException e) {
                logger.error("Job execution was interrupted: {}", e.getMessage(), e);
                exit(1);
            } catch (ExecutionException e) {
                logger.error("Error executing the job: {}", e.getMessage(), e);
                exit(1);
            }
            result.forEach(totalComplaintsByTypeAgencyDTO -> {
                logger.info("Total: {}, Type: {}, Agency: {}",
                        totalComplaintsByTypeAgencyDTO.total(),
                        totalComplaintsByTypeAgencyDTO.type(),
                        totalComplaintsByTypeAgencyDTO.agency());
            });
        });
    }
}
