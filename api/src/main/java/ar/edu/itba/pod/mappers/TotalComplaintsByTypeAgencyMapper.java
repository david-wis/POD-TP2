package ar.edu.itba.pod.mappers;

import ar.edu.itba.pod.models.Complaint;
import ar.edu.itba.pod.models.TypeAgency;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import static ar.edu.itba.pod.Globals.TYPES_MAP_NAME;

@SuppressWarnings("deprecation")
public class TotalComplaintsByTypeAgencyMapper implements Mapper<String, Complaint, TypeAgency, Long>, HazelcastInstanceAware {
    private transient HazelcastInstance hazelcastInstance;

    @Override
    public void map(String s, Complaint complaint, Context<TypeAgency, Long> context) {
        final String type = hazelcastInstance.<String, String>getMap(TYPES_MAP_NAME).get(complaint.getType());
        if (type != null) {
            final String agency = complaint.getAgency();
            final TypeAgency typeAgency = new TypeAgency(type, agency);
            context.emit(typeAgency, 1L);
        }
    }

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }
}
