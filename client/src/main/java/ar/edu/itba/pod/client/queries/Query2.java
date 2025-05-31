package ar.edu.itba.pod.client.queries;

import ar.edu.itba.pod.client.ClientUtils;
import ar.edu.itba.pod.collators.NeighborhoodQuadTypeMaxCountCollator;
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

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static java.lang.System.exit;

@SuppressWarnings("deprecation")
public class Query2 {

    private static final Logger logger = LoggerFactory.getLogger(Query2.class);

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        float q = 0.1f;

        ClientUtils.run("MostPopularType", (jobTracker, inputKeyValueSource, hazelcastInstance) -> {
            ICompletableFuture<Map<NeighborhoodQuadType, Long>> future1 = jobTracker.newJob(inputKeyValueSource)
                    .mapper(new NeighborhoodQuadTypeMapper(q))
                    .reducer(new NeighborhoodQuadTypeCountReducerFactory())
                    .submit();

            Map<NeighborhoodQuadType, Long> countMap;
            countMap = future1.get();

            IMap<NeighborhoodQuadType, Long> intermediateMap = hazelcastInstance.getMap("neighborhoodQuadTypeCountMap");
            intermediateMap.putAll(countMap);

            try (KeyValueSource<NeighborhoodQuadType, Long> kvSource = KeyValueSource.fromMap(intermediateMap)) {
                ICompletableFuture<List<NeighborhoodQuadTypeMaxCountDTO>> future2 = jobTracker.newJob(kvSource)
                        .mapper(new MostPopularTypeMapper())
                        .reducer(new MaxTypeReducerFactory())
                        .submit(new NeighborhoodQuadTypeMaxCountCollator());

                List<NeighborhoodQuadTypeMaxCountDTO> finalResult = future2.get();

                finalResult.forEach(dto ->
                        System.out.printf("%s;%d;%d;%s\n",
                                dto.neighborhood(), dto.quadLat(), dto.quadLon(), dto.type()));
            } catch (InterruptedException | ExecutionException e) {
                logger.error("Second job failed: {}", e.getMessage(), e);
                exit(1);
            }
        });
    }
}
