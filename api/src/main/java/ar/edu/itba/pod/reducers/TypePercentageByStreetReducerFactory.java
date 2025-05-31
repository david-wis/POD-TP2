package ar.edu.itba.pod.reducers;

import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

@SuppressWarnings("deprecation")
public class TypePercentageByStreetReducerFactory implements ReducerFactory<String, Integer, Double> {
    private final long total;
    public TypePercentageByStreetReducerFactory(final long total) {
        this.total = total;
    }

    @Override
    public Reducer<Integer, Double> newReducer(String key) {
        return new TypePercentageByStreetReducer(this.total);
    }

    private static class TypePercentageByStreetReducer extends Reducer<Integer, Double> {
        private int count = 0;
        private final long total;

        public TypePercentageByStreetReducer(final long total) {
            this.total = total;
        }
        @Override
        public void beginReduce() {
            count = 0;
        }

        @Override
        public void reduce(Integer value) {
            count += value;
        }

        @Override
        public Double finalizeReduce() {
            return count * 100.0 / this.total;
        }
    }
}
