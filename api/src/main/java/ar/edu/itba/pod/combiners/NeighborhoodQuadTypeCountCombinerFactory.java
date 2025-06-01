package ar.edu.itba.pod.combiners;

import ar.edu.itba.pod.models.NeighborhoodQuadType;
import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;

@SuppressWarnings("deprecation")
public class NeighborhoodQuadTypeCountCombinerFactory implements CombinerFactory<NeighborhoodQuadType, Long, Long> {

    @Override
    public Combiner<Long, Long> newCombiner(NeighborhoodQuadType key) {
        return new NeighborhoodQuadTypeCountCombiner();
    }

    private static class NeighborhoodQuadTypeCountCombiner extends Combiner<Long, Long> {
        private long total;

        @Override
        public void reset() {
            total = 0;
        }

        @Override
        public void combine(Long value) {
            total += value;
        }

        @Override
        public Long finalizeChunk() {
            return total;
        }
    }
}
