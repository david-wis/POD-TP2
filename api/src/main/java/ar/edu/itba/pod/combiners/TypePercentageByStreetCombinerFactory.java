package ar.edu.itba.pod.combiners;

import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;

@SuppressWarnings("deprecation")
public class TypePercentageByStreetCombinerFactory implements CombinerFactory<String, Integer, Integer> {

    @Override
    public Combiner<Integer, Integer> newCombiner(String key) {
        return new TypePercentageByStreetCombiner();
    }

    private static class TypePercentageByStreetCombiner extends Combiner<Integer, Integer> {
        private int count = 0;

        @Override
        public void reset() {
            count = 0;
        }

        @Override
        public void combine(Integer value) {
            count += value;
        }

        @Override
        public Integer finalizeChunk() {
            return count;
        }
    }
}
