package ar.edu.itba.pod.client.queries;

import ar.edu.itba.pod.client.ClientUtils;
import ar.edu.itba.pod.collators.AgencyDateMovingAverageCollator;
import ar.edu.itba.pod.mappers.AgencyDateMovingAverageMapper;
import ar.edu.itba.pod.models.dto.AgencyDateMovingAverageDTO;
import ar.edu.itba.pod.reducers.AgencyDateMovingAverageReducerFactory;
import com.hazelcast.core.ICompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Query3 {

    private static final Logger logger = LoggerFactory.getLogger(Query3.class);

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        final int WINDOW_SIZE = 3;

        ClientUtils.run("AgencyDateMovingAverage", (jobTracker, keyValueSource) -> {
            ICompletableFuture<List<AgencyDateMovingAverageDTO> > futureResponse = jobTracker.newJob(keyValueSource)
                    .mapper(new AgencyDateMovingAverageMapper(WINDOW_SIZE))
                    .reducer(new AgencyDateMovingAverageReducerFactory(WINDOW_SIZE))
                    .submit(new AgencyDateMovingAverageCollator());

            List<AgencyDateMovingAverageDTO> result = null;
            try {
                result = futureResponse.get();
            } catch (InterruptedException e) {
                logger.error("Job execution was interrupted: {}", e.getMessage(), e);
                System.exit(1);
            } catch (ExecutionException e) {
                logger.error("Error executing the job: {}", e.getMessage(), e);
                System.exit(1);
            }
            logger.info("Job execution completed: {}", result.size());
            result.forEach(agencyDateMovingAverageDTO -> System.out.printf("%s;%d;%d;%.2f%n",
                    agencyDateMovingAverageDTO.agency(),
                    agencyDateMovingAverageDTO.year(),
                    agencyDateMovingAverageDTO.month(),
                    agencyDateMovingAverageDTO.movingAverage()));
        });

    }

}
