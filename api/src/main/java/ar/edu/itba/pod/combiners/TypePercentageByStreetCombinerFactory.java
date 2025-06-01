package ar.edu.itba.pod.combiners;

import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;

@SuppressWarnings("deprecation")
public class TypePercentageByStreetCombinerFactory implements CombinerFactory<String, Integer, Long> {

    @Override
    public Combiner<Integer, Long> newCombiner(String key) {
        return new TypePercentageByStreetCombiner();
    }

    private static class TypePercentageByStreetCombiner extends Combiner<Integer, Long> {
        private long count = 0;

        @Override
        public void reset() {
            count = 0;
        }

        @Override
        public void combine(Integer value) {
            count += value;
        }

        @Override
        public Long finalizeChunk() {
            return count;
        }
    }
}
