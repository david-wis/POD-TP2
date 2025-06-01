package ar.edu.itba.pod.client.queries;

import ar.edu.itba.pod.client.ArgumentParser;
import ar.edu.itba.pod.client.ClientUtils;
import ar.edu.itba.pod.client.CsvManager;
import ar.edu.itba.pod.collators.TotalComplaintsByTypeAgencyCollator;
import ar.edu.itba.pod.combiners.TotalComplaintsByTypeAgencyCombinerFactory;
import ar.edu.itba.pod.mappers.TotalComplaintsByTypeAgencyMapper;
import ar.edu.itba.pod.models.dto.TotalComplaintsByTypeAgencyDTO;
import ar.edu.itba.pod.reducers.TotalComplaintsByTypeAgencyReducerFactory;
import com.hazelcast.core.ICompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

import static ar.edu.itba.pod.client.Constants.OUT_PATH_ARG;
import static ar.edu.itba.pod.client.Constants.QUERY1_HEADERS;
import static java.lang.System.exit;

public class Query1 {

    private static final Logger logger = LoggerFactory.getLogger(Query1.class);

    public static void main(String[] args) {
        ClientUtils.run("TotalComplaintsByTypeAgency", (jobTracker, keyValueSource, hazelcastInstance) -> {
            ICompletableFuture<List<TotalComplaintsByTypeAgencyDTO>> futureResponse = jobTracker.newJob(keyValueSource)
                    .mapper(new TotalComplaintsByTypeAgencyMapper())
                    .combiner(new TotalComplaintsByTypeAgencyCombinerFactory())
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

            final String output = ArgumentParser.getStringArg(OUT_PATH_ARG);
            final Path outputPath = Paths.get(output, "query1.txt");

            Stream<String> linesStream = result.stream().map(t -> String.format("%s;%s;%d", t.type(), t.agency(), t.total()));
            CsvManager.writeLines(outputPath, Stream.concat(Stream.of(QUERY1_HEADERS), linesStream));
        });
    }
}
