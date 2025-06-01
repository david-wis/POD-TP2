package ar.edu.itba.pod.reducers;

import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;

@SuppressWarnings("deprecation")
public class TypePercentageByStreetReducerFactory implements ReducerFactory<String, Long, BigDecimal> {
    private final long total;

    public TypePercentageByStreetReducerFactory(final long total) {
        this.total = total;
    }

    @Override
    public Reducer<Long, BigDecimal> newReducer(String key) {
        return new TypePercentageByStreetReducer(this.total);
    }

    private static class TypePercentageByStreetReducer extends Reducer<Long, BigDecimal> {
        private long count = 0;
        private final long total;

        public TypePercentageByStreetReducer(final long total) {
            this.total = total;
        }
        @Override
        public void beginReduce() {
            count = 0;
        }

        @Override
        public void reduce(Long value) {
            count += value;
        }

        @Override
        public BigDecimal finalizeReduce() {
            return BigDecimal.valueOf(count * 100.0).divide(BigDecimal.valueOf(total), 2, RoundingMode.DOWN);
        }
    }
}
