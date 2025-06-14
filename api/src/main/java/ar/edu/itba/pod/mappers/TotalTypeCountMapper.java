package ar.edu.itba.pod.mappers;

import ar.edu.itba.pod.models.Complaint;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import static ar.edu.itba.pod.Globals.TYPES_MAP_NAME;

@SuppressWarnings("deprecation")
public class TotalTypeCountMapper implements Mapper<String, Complaint, String, Integer>, HazelcastInstanceAware {
    private transient HazelcastInstance hazelcastInstance;
    @Override
    public void map(String s, Complaint complaint, Context<String, Integer> context) {
        if (hazelcastInstance.<String, String>getMap(TYPES_MAP_NAME).containsKey(complaint.getType())) {
            context.emit(complaint.getType(), 1);
        }
    }

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }
}
