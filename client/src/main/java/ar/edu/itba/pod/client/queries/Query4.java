package ar.edu.itba.pod.client.queries;

import ar.edu.itba.pod.client.ClientUtils;
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

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

public class Query4 {
    private static final Logger logger = LoggerFactory.getLogger(Query1.class);

    @SuppressWarnings("deprecation")
    public static void main(String[] args) throws IOException, CancellationException, InterruptedException, ExecutionException {
        ClientUtils.run("TotalTypeCount",
                (jobTracker, inputKeyValueSource, hazelcastInstance) -> {
                ICompletableFuture<Long> futureTotalTypeCount = jobTracker.newJob(inputKeyValueSource)
                        .mapper(new TotalTypeCountMapper())
                        .combiner(new TotalTypeCountCombinerFactory())
                        .reducer(new TotalTypeCountReducerFactory())
                        .submit(new TotalTypeCountCollator());
                ICompletableFuture<Map<TypeStreet, Integer>> futureTypeStreetMap = jobTracker.newJob(inputKeyValueSource)
                        .mapper(new TypeStreetMapper())
                        .combiner(new TypeStreetUniqueCombinerFactory())
                        .reducer(new TypeStreetUniqueReducerFactory())
                        .submit();
                long totalTypes;
                IMap<TypeStreet, Integer> typeStreetMap = hazelcastInstance.getMap("typeStreetMap");

                totalTypes = futureTotalTypeCount.get();
                logger.info("Total types count: {}", totalTypes);
                typeStreetMap.putAll(futureTypeStreetMap.get());
                try (KeyValueSource<TypeStreet, Integer> keyValueSource = KeyValueSource.fromMap(typeStreetMap)) {
                    ICompletableFuture<List<TypePercentageByStreetDTO>> futureTypePercentageByStreet = jobTracker.newJob(keyValueSource).
                            mapper(new StreetMapper())
                            .combiner(new TypePercentageByStreetCombinerFactory())
                            .reducer(new TypePercentageByStreetReducerFactory(totalTypes))
                            .submit(new TypePercentageByStreetCollator());
                    List<TypePercentageByStreetDTO> result = futureTypePercentageByStreet.get();
                    result.forEach(typePercentageByStreetDTO ->
                        logger.info("Street: {}, percentage: {}",
                                typePercentageByStreetDTO.street(),
                                typePercentageByStreetDTO.percentage())
                    );
                }
            }
        );
    }
}
