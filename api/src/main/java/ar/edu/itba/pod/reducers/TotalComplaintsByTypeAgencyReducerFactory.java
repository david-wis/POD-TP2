package ar.edu.itba.pod.reducers;

import ar.edu.itba.pod.models.TypeAgency;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

@SuppressWarnings("deprecation")
public class TotalComplaintsByTypeAgencyReducerFactory implements ReducerFactory<TypeAgency, Integer, Integer> {
    @Override
    public Reducer<Integer, Integer> newReducer(TypeAgency typeAgency) {
        return new TotalComplaintsByTypeAgencyReducer();
    }

    private static class TotalComplaintsByTypeAgencyReducer extends Reducer<Integer, Integer> {
        private int total = 0;

        @Override
        public void beginReduce() {
            total = 0;
        }

        @Override
        public void reduce(Integer value) {
            total += value;
        }


        @Override
        public Integer finalizeReduce() {
            return total;
        }
    }
}
