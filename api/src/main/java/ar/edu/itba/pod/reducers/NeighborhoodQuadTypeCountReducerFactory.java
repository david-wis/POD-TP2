package ar.edu.itba.pod.reducers;

import ar.edu.itba.pod.models.NeighborhoodQuadType;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

@SuppressWarnings("deprecation")
public class NeighborhoodQuadTypeCountReducerFactory implements ReducerFactory<NeighborhoodQuadType, Long, Long> {
    @Override
    public Reducer<Long, Long> newReducer(NeighborhoodQuadType neighborhoodQuadType) {
        return new NeighborhoodQuadTypeCountReducer();
    }

    private static class NeighborhoodQuadTypeCountReducer extends Reducer<Long, Long> {
        private long total = 0;

        @Override
        public void beginReduce() {
            total = 0;
        }

        @Override
        public void reduce(Long value) {
            total += value;
        }

        @Override
        public Long finalizeReduce() {
            return total;
        }
    }
}
