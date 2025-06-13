package ar.edu.itba.pod.combiners;

import ar.edu.itba.pod.models.TypeStreet;
import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;

@SuppressWarnings("deprecation")
public class TypeStreetUniqueCombinerFactory implements CombinerFactory<TypeStreet, Integer, Integer> {

    @Override
    public Combiner<Integer, Integer> newCombiner(TypeStreet key) {
        return new TypeStreetUniqueCombiner();
    }

    private static class TypeStreetUniqueCombiner extends Combiner<Integer, Integer> {
        private int max;

        @Override
        public void reset() {
            max = 0;
        }

        @Override
        public void combine(Integer value) {
            max = 1;
        }

        @Override
        public Integer finalizeChunk() {
            return max;
        }
    }
}
