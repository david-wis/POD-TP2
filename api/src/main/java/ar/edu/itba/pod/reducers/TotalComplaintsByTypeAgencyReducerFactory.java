package ar.edu.itba.pod.reducers;

import ar.edu.itba.pod.models.TypeAgency;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

@SuppressWarnings("deprecation")
public class TotalComplaintsByTypeAgencyReducerFactory implements ReducerFactory<TypeAgency, Long, Long> {
    @Override
    public Reducer<Long, Long> newReducer(TypeAgency typeAgency) {
        return new TotalComplaintsByTypeAgencyReducer();
    }

    private static class TotalComplaintsByTypeAgencyReducer extends Reducer<Long, Long> {
        private long total = 0;

        @Override
        public void beginReduce() {
            total = 0;
        }

        @Override
        public void reduce(Long value) {
            total += value;
        }


        @Override
        public Long finalizeReduce() {
            return total;
        }
    }
}
