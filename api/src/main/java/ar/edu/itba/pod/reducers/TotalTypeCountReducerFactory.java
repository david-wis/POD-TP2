package ar.edu.itba.pod.reducers;

import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

@SuppressWarnings("deprecation")
public class TotalTypeCountReducerFactory implements ReducerFactory<String, Integer, Integer> {
    @Override
    public Reducer<Integer, Integer> newReducer(String key) {
        return new TotalTypeCountReducer();
    }

    private static class TotalTypeCountReducer extends Reducer<Integer, Integer> {
        private int exists;

        @Override
        public void beginReduce() {
            exists = 0;
        }

        @Override
        public void reduce(Integer value) {
            exists = 1;
        }

        @Override
        public Integer finalizeReduce() {
            return exists;
        }
    }
}
