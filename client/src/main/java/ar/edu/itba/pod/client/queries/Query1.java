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

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import static ar.edu.itba.pod.Globals.TOTAL_COMPLAINTS_BY_TYPE_AGENCY_JOB_TRACKER_NAME;
import static ar.edu.itba.pod.client.Constants.*;

public class Query1 {

    private static final Logger logger = LoggerFactory.getLogger(Query1.class);
    private static final int QUERY_NUM = 1;

    public static void main(String[] args) {
        ClientUtils.run(TOTAL_COMPLAINTS_BY_TYPE_AGENCY_JOB_TRACKER_NAME , QUERY_NUM, (jobTracker, keyValueSource, hazelcastInstance, customLogger) -> {
            ClientUtils.loadTypes(hazelcastInstance);
            customLogger.info("Inicio del trabajo map/reduce");
            ICompletableFuture<List<TotalComplaintsByTypeAgencyDTO>> futureResponse = jobTracker.newJob(keyValueSource)
                    .mapper(new TotalComplaintsByTypeAgencyMapper())
                    .combiner(new TotalComplaintsByTypeAgencyCombinerFactory())
                    .reducer(new TotalComplaintsByTypeAgencyReducerFactory())
                    .submit(new TotalComplaintsByTypeAgencyCollator());
            List<TotalComplaintsByTypeAgencyDTO> result = futureResponse.get();

            final String output = ArgumentParser.getStringArg(OUT_PATH_ARG);
            final Path outputPath = Paths.get(output, String.format(QUERY_FILE_TEMPLATE, QUERY_NUM));
            Stream<String> linesStream = result.stream().map(t -> String.format("%s;%s;%d", t.type(), t.agency(), t.total()));
            CsvManager.writeLines(outputPath, Stream.concat(Stream.of(QUERY1_HEADERS), linesStream));
            customLogger.info("Fin del trabajo map/reduce");
        });
    }
}
