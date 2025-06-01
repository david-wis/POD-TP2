package ar.edu.itba.pod.mappers;

import ar.edu.itba.pod.models.Complaint;
import ar.edu.itba.pod.models.TypeStreet;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

@SuppressWarnings("deprecation")
public class TypeStreetMapper implements Mapper<String, Complaint, TypeStreet, Integer>, HazelcastInstanceAware {
    private transient HazelcastInstance hazelcastInstance;
    private final String neighborhood;

    public TypeStreetMapper(final String neighborhood) {
        this.neighborhood = neighborhood;
    }

    @Override
    public void map(String key, Complaint complaint, Context<TypeStreet, Integer> context) {
        if (hazelcastInstance.<String>getList("types").contains(complaint.getType()) && complaint.getNeighborhood().equals(neighborhood)) {
            context.emit(new TypeStreet(complaint), 1);
        }
    }

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }
}
