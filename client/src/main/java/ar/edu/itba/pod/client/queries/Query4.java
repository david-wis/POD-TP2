package ar.edu.itba.pod.client.queries;

import ar.edu.itba.pod.client.ClientUtils;
import ar.edu.itba.pod.collators.TotalTypeCountCollator;
import ar.edu.itba.pod.mappers.StreetMapper;
import ar.edu.itba.pod.mappers.TotalTypeCountMapper;
import ar.edu.itba.pod.mappers.TypeStreetMapper;
import ar.edu.itba.pod.models.Complaint;
import ar.edu.itba.pod.models.TypeStreet;
import ar.edu.itba.pod.reducers.TotalTypeCountReducerFactory;
import ar.edu.itba.pod.reducers.TypePercentageByStreetReducer;
import ar.edu.itba.pod.reducers.TypeStreetUniqueReducerFactory;
import com.hazelcast.core.ICompletableFuture;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

public class Query4 {
    private static final Logger logger = LoggerFactory.getLogger(Query1.class);

    public static void main(String[] args) throws IOException, CancellationException, InterruptedException, ExecutionException {
        ClientUtils.run("TotalTypeCount",
                (jobTracker, inputKeyValueSource, hazelcastInstance) -> {
                ICompletableFuture<Long> future1 = jobTracker.newJob(inputKeyValueSource)
                        .mapper(new TotalTypeCountMapper())
                        .reducer(new TotalTypeCountReducerFactory())
                        .submit(new TotalTypeCountCollator());
                ICompletableFuture<Map<TypeStreet, Integer>> future2 = jobTracker.newJob(inputKeyValueSource)
                        .mapper(new TypeStreetMapper())
                        .reducer(new TypeStreetUniqueReducerFactory())
                        .submit();
                long totalTypes;
                IMap<TypeStreet, Integer> typeStreetMap = hazelcastInstance.getMap("typeStreetMap");

                totalTypes = future1.get();
                typeStreetMap.putAll(future2.get());
                try (KeyValueSource<TypeStreet, Integer> keyValueSource = KeyValueSource.fromMap(typeStreetMap)) {
//                    ICompletableFuture<Long> future3 = jobTracker.newJob(keyValueSource).
//                            mapper(new StreetMapper())
//                            .reducer(new TypePercentageByStreetReducer(totalTypes))
//                            .submit(new TotalTypeCountCollator());
                }
            }
        );
    }
}
