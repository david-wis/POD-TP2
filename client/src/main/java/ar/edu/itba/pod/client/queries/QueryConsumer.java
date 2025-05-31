package ar.edu.itba.pod.client.queries;

import ar.edu.itba.pod.models.Complaint;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@FunctionalInterface
public interface QueryConsumer{

    @SuppressWarnings("deprecation")
    void accept(JobTracker jobTracker, KeyValueSource<String, Complaint> inputKeyValueSource, HazelcastInstance hazelcastInstance) throws InterruptedException, ExecutionException, IOException;

}
