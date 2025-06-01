package ar.edu.itba.pod.mappers;

import ar.edu.itba.pod.models.Complaint;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

@SuppressWarnings("deprecation")
public class TotalTypeCountMapper implements Mapper<String, Complaint, String, Boolean>, HazelcastInstanceAware {
    private transient HazelcastInstance hazelcastInstance;
    @Override
    public void map(String s, Complaint complaint, Context<String, Boolean> context) {
        if (hazelcastInstance.<String>getList("types").contains(complaint.getType())) {
            context.emit(complaint.getType(), true);
        }
    }

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }
}
