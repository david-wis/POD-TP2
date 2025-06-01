package ar.edu.itba.pod.combiners;

import ar.edu.itba.pod.models.NeighborhoodQuad;
import ar.edu.itba.pod.models.TypeCount;
import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;

@SuppressWarnings("deprecation")
public class MaxTypeCombinerFactory implements CombinerFactory<NeighborhoodQuad, TypeCount, TypeCount> {

    @Override
    public Combiner<TypeCount, TypeCount> newCombiner(NeighborhoodQuad key) {
        return new MaxTypeCombiner();
    }

    private static class MaxTypeCombiner extends Combiner<TypeCount, TypeCount> {
        private TypeCount max;

        @Override
        public void reset() {
            max = null;
        }

        @Override
        public void combine(TypeCount value) {
            if (max == null || value.getCount() > max.getCount()) {
                max = value;
            }
        }

        @Override
        public TypeCount finalizeChunk() {
            return max;
        }
    }
}
