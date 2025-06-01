package ar.edu.itba.pod.combiners;

import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;

@SuppressWarnings("deprecation")
public class TotalTypeCountCombinerFactory implements CombinerFactory<String, Boolean, Boolean> {

    @Override
    public Combiner<Boolean, Boolean> newCombiner(String key) {
        return new TotalTypeCountCombiner();
    }

    private static class TotalTypeCountCombiner extends Combiner<Boolean, Boolean> {
        private boolean exists;

        @Override
        public void reset() {
            exists = false;
        }

        @Override
        public void combine(Boolean value) {
            exists |= value;
        }

        @Override
        public Boolean finalizeChunk() {
            return exists;
        }
    }
}
