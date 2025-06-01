package ar.edu.itba.pod.combiners;

import ar.edu.itba.pod.models.AgencyDate;
import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;

@SuppressWarnings("deprecation")
public class AgencyDateMovingAverageCombinerFactory implements CombinerFactory<AgencyDate, Long, Long> {

    @Override
    public Combiner<Long, Long> newCombiner(AgencyDate agencyDate) {
        return new AgencyDateMovingAverageCombiner();
    }

    private static class AgencyDateMovingAverageCombiner extends Combiner<Long, Long> {
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
