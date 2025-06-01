package ar.edu.itba.pod.mappers;

import ar.edu.itba.pod.models.Complaint;
import ar.edu.itba.pod.models.NeighborhoodQuadType;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import static ar.edu.itba.pod.Globals.TYPES_MAP_NAME;

@SuppressWarnings("deprecation")
public class NeighborhoodQuadTypeMapper implements Mapper<String, Complaint, NeighborhoodQuadType, Long>, HazelcastInstanceAware {
    private final float q;
    private transient HazelcastInstance hazelcastInstance;

    public NeighborhoodQuadTypeMapper(float q) {
        this.q = q;
    }

    @Override
    public void map(String s, Complaint complaint, Context<NeighborhoodQuadType, Long> context) {
        final String type = hazelcastInstance.<String, String>getMap(TYPES_MAP_NAME).get(complaint.getType());
        if (type == null) return; // Ignore complaints with unknown types

        int quadLat = (int) Math.floor(complaint.getLatitude() / q);
        int quadLon = (int) Math.floor(complaint.getLongitude() / q);
        NeighborhoodQuadType nq = new NeighborhoodQuadType(complaint.getNeighborhood(), quadLat, quadLon, type);
        context.emit(nq, 1L);
    }

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }
}