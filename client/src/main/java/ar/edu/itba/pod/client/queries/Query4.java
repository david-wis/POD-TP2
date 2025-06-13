package ar.edu.itba.pod.client.queries;

import ar.edu.itba.pod.client.ArgumentParser;
import ar.edu.itba.pod.client.ClientUtils;
import ar.edu.itba.pod.client.CsvManager;
import ar.edu.itba.pod.collators.TotalTypeCountCollator;
import ar.edu.itba.pod.collators.TypePercentageByStreetCollator;
import ar.edu.itba.pod.combiners.TotalTypeCountCombinerFactory;
import ar.edu.itba.pod.combiners.TypePercentageByStreetCombinerFactory;
import ar.edu.itba.pod.combiners.TypeStreetUniqueCombinerFactory;
import ar.edu.itba.pod.mappers.StreetMapper;
import ar.edu.itba.pod.mappers.TotalTypeCountMapper;
import ar.edu.itba.pod.mappers.TypeStreetMapper;
import ar.edu.itba.pod.models.TypeStreet;
import ar.edu.itba.pod.models.dto.TypePercentageByStreetDTO;
import ar.edu.itba.pod.reducers.TotalTypeCountReducerFactory;
import ar.edu.itba.pod.reducers.TypePercentageByStreetReducerFactory;
import ar.edu.itba.pod.reducers.TypeStreetUniqueReducerFactory;
import com.hazelcast.core.ICompletableFuture;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.KeyValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CancellationException;
import java.util.stream.Stream;

import static ar.edu.itba.pod.Globals.TOTAL_TYPE_COUNT_JOB_TRACKER_NAME;
import static ar.edu.itba.pod.Globals.TYPE_STREET_MAP_NAME;
import static ar.edu.itba.pod.client.Constants.*;

public class Query4 {
    private static final Logger logger = LoggerFactory.getLogger(Query4.class);
    private static final int QUERY_NUM = 4;

    @SuppressWarnings("deprecation")
    public static void main(String[] args) throws CancellationException {
        ClientUtils.run(TOTAL_TYPE_COUNT_JOB_TRACKER_NAME, QUERY_NUM,
            (jobTracker, inputKeyValueSource, hazelcastInstance, customLogger) -> {
                ClientUtils.loadTypes(hazelcastInstance);
                String neighborhood = ArgumentParser.getStringArg(NEIGHBOURHOOD_ARG).replace('_', ' ');

                customLogger.info("Inicio del trabajo 1 map/reduce");
                ICompletableFuture<Long> futureTotalTypeCount = jobTracker.newJob(inputKeyValueSource)
                        .mapper(new TotalTypeCountMapper())
                        .combiner(new TotalTypeCountCombinerFactory())
                        .reducer(new TotalTypeCountReducerFactory())
                        .submit(new TotalTypeCountCollator());
                customLogger.info("Fin del trabajo 1 map/reduce");
                customLogger.info("Inicio del trabajo 2 map/reduce");
                ICompletableFuture<Map<TypeStreet, Integer>> futureTypeStreetMap = jobTracker.newJob(inputKeyValueSource)
                        .mapper(new TypeStreetMapper(neighborhood))
                        .combiner(new TypeStreetUniqueCombinerFactory())
                        .reducer(new TypeStreetUniqueReducerFactory())
                        .submit();
                customLogger.info("Fin del trabajo 2 map/reduce");

                IMap<TypeStreet, Integer> typeStreetMap = hazelcastInstance.getMap(TYPE_STREET_MAP_NAME);
                typeStreetMap.putAll(futureTypeStreetMap.get());

                long totalTypes = futureTotalTypeCount.get();

                try (KeyValueSource<TypeStreet, Integer> keyValueSource = KeyValueSource.fromMap(typeStreetMap)) {
                    customLogger.info("Inicio del trabajo 3 map/reduce");
                    ICompletableFuture<List<TypePercentageByStreetDTO>> futureTypePercentageByStreet = jobTracker.newJob(keyValueSource)
                            .mapper(new StreetMapper())
                            .combiner(new TypePercentageByStreetCombinerFactory())
                            .reducer(new TypePercentageByStreetReducerFactory(totalTypes))
                            .submit(new TypePercentageByStreetCollator());
                    List<TypePercentageByStreetDTO> result = futureTypePercentageByStreet.get();

                    final String output = ArgumentParser.getStringArg(OUT_PATH_ARG);
                    final Path outputPath = Paths.get(output, String.format(QUERY_FILE_TEMPLATE, QUERY_NUM));
                    logger.info("Writing results to {} {}", result.size(), neighborhood);
                    Stream<String> linesStream = result.stream().map(t -> String.format("%s;%.2f%%", t.street(), t.percentage()));
                    CsvManager.writeLines(outputPath, Stream.concat(Stream.of(QUERY4_HEADERS), linesStream));
                    customLogger.info("Fin del trabajo 3 map/reduce");
                }
                typeStreetMap.destroy();
            }
        );
    }
}
