package ar.edu.itba.pod.client.queries;

import ar.edu.itba.pod.client.ArgumentParser;
import ar.edu.itba.pod.client.ClientUtils;
import ar.edu.itba.pod.client.CsvManager;
import ar.edu.itba.pod.collators.NeighborhoodQuadTypeMaxCountCollator;
import ar.edu.itba.pod.combiners.MaxTypeCombinerFactory;
import ar.edu.itba.pod.combiners.NeighborhoodQuadTypeCountCombinerFactory;
import ar.edu.itba.pod.mappers.MostPopularTypeMapper;
import ar.edu.itba.pod.mappers.NeighborhoodQuadTypeMapper;
import ar.edu.itba.pod.models.NeighborhoodQuadType;
import ar.edu.itba.pod.models.dto.NeighborhoodQuadTypeMaxCountDTO;
import ar.edu.itba.pod.reducers.MaxTypeReducerFactory;
import ar.edu.itba.pod.reducers.NeighborhoodQuadTypeCountReducerFactory;
import com.hazelcast.core.ICompletableFuture;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.KeyValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static ar.edu.itba.pod.Globals.MOST_POPULAR_TYPE_JOB_TRACKER_NAME;
import static ar.edu.itba.pod.Globals.NEIGHBOURHOOD_TYPE_COUNT_MAP_NAME;
import static ar.edu.itba.pod.client.Constants.*;

@SuppressWarnings("deprecation")
public class Query2 {

    private static final Logger logger = LoggerFactory.getLogger(Query2.class);
    private static final int QUERY_NUM = 2;

    public static void main(String[] args) {
        final float q = ArgumentParser.getFloatArg(Q_ARG, Q_MIN, Q_MAX);

        ClientUtils.run(MOST_POPULAR_TYPE_JOB_TRACKER_NAME , QUERY_NUM, (jobTracker, inputKeyValueSource, hazelcastInstance, customLogger) -> {
            ClientUtils.loadTypes(hazelcastInstance);
            customLogger.info("Inicio del trabajo 1 map/reduce");
            ICompletableFuture<Map<NeighborhoodQuadType, Long>> future1 = jobTracker.newJob(inputKeyValueSource)
                    .mapper(new NeighborhoodQuadTypeMapper(q))
                    .combiner(new NeighborhoodQuadTypeCountCombinerFactory())
                    .reducer(new NeighborhoodQuadTypeCountReducerFactory())
                    .submit();
            Map<NeighborhoodQuadType, Long> countMap = future1.get();
            customLogger.info("Fin del trabajo 1 map/reduce");

            IMap<NeighborhoodQuadType, Long> intermediateMap = hazelcastInstance.getMap(NEIGHBOURHOOD_TYPE_COUNT_MAP_NAME);
            intermediateMap.putAll(countMap);

            try (KeyValueSource<NeighborhoodQuadType, Long> kvSource = KeyValueSource.fromMap(intermediateMap)) {
                customLogger.info("Inicio del trabajo 2 map/reduce");
                ICompletableFuture<List<NeighborhoodQuadTypeMaxCountDTO>> future2 = jobTracker.newJob(kvSource)
                        .mapper(new MostPopularTypeMapper())
                        .combiner(new MaxTypeCombinerFactory())
                        .reducer(new MaxTypeReducerFactory())
                        .submit(new NeighborhoodQuadTypeMaxCountCollator());
                List<NeighborhoodQuadTypeMaxCountDTO> result = future2.get();
                customLogger.info("Fin del trabajo 2 map/reduce");

                final String output = ArgumentParser.getStringArg(OUT_PATH_ARG);
                final Path outputPath = Paths.get(output, String.format(QUERY_FILE_TEMPLATE, QUERY_NUM));
                Stream<String> linesStream = result.stream().map(dto -> String.format("%s;%d;%d;%s", dto.neighborhood(), dto.quadLat(), dto.quadLon(), dto.type()));
                CsvManager.writeLines(outputPath, Stream.concat(Stream.of(QUERY2_HEADERS), linesStream));
            }
        });
    }
}
