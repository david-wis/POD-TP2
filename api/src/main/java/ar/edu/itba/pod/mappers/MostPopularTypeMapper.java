package ar.edu.itba.pod.mappers;

import ar.edu.itba.pod.models.NeighborhoodQuad;
import ar.edu.itba.pod.models.NeighborhoodQuadType;
import ar.edu.itba.pod.models.TypeCount;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

@SuppressWarnings("deprecation")
public class MostPopularTypeMapper implements Mapper<NeighborhoodQuadType, Long, NeighborhoodQuad, TypeCount> {

    @Override
    public void map(NeighborhoodQuadType neighborhoodQuadType, Long count, Context<NeighborhoodQuad, TypeCount> context) {
        NeighborhoodQuad neighborhoodQuad = new NeighborhoodQuad(neighborhoodQuadType.getNeighborhood(),
                neighborhoodQuadType.getQuadLat(), neighborhoodQuadType.getQuadLon());
        TypeCount typeCount = new TypeCount(neighborhoodQuadType.getType(), count);
        context.emit(neighborhoodQuad, typeCount);
    }
}