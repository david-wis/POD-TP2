package ar.edu.itba.pod.combiners;

import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;

@SuppressWarnings("deprecation")
public class TotalTypeCountCombinerFactory implements CombinerFactory<String, Integer, Integer> {

    @Override
    public Combiner<Integer, Integer> newCombiner(String key) {
        return new TotalTypeCountCombiner();
    }

    private static class TotalTypeCountCombiner extends Combiner<Integer, Integer> {
        private int exists;

        @Override
        public void reset() {
            exists = 0;
        }

        @Override
        public void combine(Integer value) {
            exists = 1;
        }

        @Override
        public Integer finalizeChunk() {
            return exists;
        }
    }
}
