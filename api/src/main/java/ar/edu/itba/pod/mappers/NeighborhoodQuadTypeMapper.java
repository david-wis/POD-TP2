package ar.edu.itba.pod.mappers;

import ar.edu.itba.pod.models.Complaint;
import ar.edu.itba.pod.models.NeighborhoodQuadType;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

@SuppressWarnings("deprecation")
public class NeighborhoodQuadTypeMapper implements Mapper<String, Complaint, NeighborhoodQuadType, Long> {
    private final float q;

    public NeighborhoodQuadTypeMapper(float q) {
        this.q = q;
    }

    @Override
    public void map(String s, Complaint complaint, Context<NeighborhoodQuadType, Long> context) {
        int quadLat = (int) Math.floor(complaint.getLatitude() / q);
        int quadLon = (int) Math.floor(complaint.getLongitude() / q);
        NeighborhoodQuadType nq = new NeighborhoodQuadType(complaint.getNeighborhood(), quadLat, quadLon, complaint.getType());
        context.emit(nq, 1L);
    }
}