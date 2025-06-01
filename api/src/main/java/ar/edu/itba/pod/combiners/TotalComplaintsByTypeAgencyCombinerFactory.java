package ar.edu.itba.pod.combiners;

import ar.edu.itba.pod.models.TypeAgency;
import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;

@SuppressWarnings("deprecation")
public class TotalComplaintsByTypeAgencyCombinerFactory implements CombinerFactory<TypeAgency, Integer, Integer> {

    @Override
    public Combiner<Integer, Integer> newCombiner(TypeAgency key) {
        return new TotalComplaintsByTypeAgencyCombiner();
    }

    private static class TotalComplaintsByTypeAgencyCombiner extends Combiner<Integer, Integer> {
        private int total;

        @Override
        public void reset() {
            total = 0;
        }

        @Override
        public void combine(Integer value) {
            total += value;
        }

        @Override
        public Integer finalizeChunk() {
            return total;
        }
    }
}
