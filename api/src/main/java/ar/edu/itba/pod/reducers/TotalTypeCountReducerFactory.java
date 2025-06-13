package ar.edu.itba.pod.reducers;

import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

@SuppressWarnings("deprecation")
public class TotalTypeCountReducerFactory implements ReducerFactory<String, Boolean, Boolean> {
    @Override
    public Reducer<Boolean, Boolean> newReducer(String key) {
        return new TotalTypeCountReducer();
    }

    private static class TotalTypeCountReducer extends Reducer<Boolean, Boolean> {
        private boolean exists;

        @Override
        public void beginReduce() {
            exists = false;
        }

        @Override
        public void reduce(Boolean value) {
            exists = true;
        }

        @Override
        public Boolean finalizeReduce() {
            return exists;
        }
    }
}
