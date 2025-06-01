package ar.edu.itba.pod.client.queries;

import ar.edu.itba.pod.client.ArgumentParser;
import ar.edu.itba.pod.client.ClientUtils;
import ar.edu.itba.pod.client.CsvManager;
import ar.edu.itba.pod.collators.AgencyDateMovingAverageCollator;
import ar.edu.itba.pod.combiners.AgencyDateMovingAverageCombinerFactory;
import ar.edu.itba.pod.mappers.AgencyDateMovingAverageMapper;
import ar.edu.itba.pod.models.dto.AgencyDateMovingAverageDTO;
import ar.edu.itba.pod.reducers.AgencyDateMovingAverageReducerFactory;
import com.hazelcast.core.ICompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

import static ar.edu.itba.pod.client.Constants.*;

public class Query3 {

    private static final Logger logger = LoggerFactory.getLogger(Query3.class);

    public static void main(String[] args) {

        final int windowSize = ArgumentParser.getIntegerArg(W_ARG, W_MIN, W_MAX);

        ClientUtils.run("AgencyDateMovingAverage", (jobTracker, keyValueSource, hazelcastInstance) -> {
            logger.info("Inicio del trabajo map/reduce");
            ICompletableFuture<List<AgencyDateMovingAverageDTO> > futureResponse = jobTracker.newJob(keyValueSource)
                    .mapper(new AgencyDateMovingAverageMapper(windowSize))
                    .combiner(new AgencyDateMovingAverageCombinerFactory())
                    .reducer(new AgencyDateMovingAverageReducerFactory(windowSize))
                    .submit(new AgencyDateMovingAverageCollator());
            List<AgencyDateMovingAverageDTO> result = null;
            result = futureResponse.get();
            logger.info("Inicio del trabajo map/reduce");

            final String output = ArgumentParser.getStringArg(OUT_PATH_ARG);
            final Path outputPath = Paths.get(output, String.format(QUERY_FILE_TEMPLATE, 3));
            Stream<String> linesStream = result.stream()
                    .map(dto -> String.format("%s;%d;%d;%.2f%n",
                            dto.agency(), dto.year(), dto.month(), dto.movingAverage()));
            CsvManager.writeLines(outputPath, Stream.concat(Stream.of(QUERY3_HEADERS), linesStream));
        });

    }

}
