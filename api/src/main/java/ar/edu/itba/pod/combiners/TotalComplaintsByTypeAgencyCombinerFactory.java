package ar.edu.itba.pod.combiners;

import ar.edu.itba.pod.models.TypeAgency;
import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;

@SuppressWarnings("deprecation")
public class TotalComplaintsByTypeAgencyCombinerFactory implements CombinerFactory<TypeAgency, Long, Long> {

    @Override
    public Combiner<Long, Long> newCombiner(TypeAgency key) {
        return new TotalComplaintsByTypeAgencyCombiner();
    }

    private static class TotalComplaintsByTypeAgencyCombiner extends Combiner<Long, Long> {
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
