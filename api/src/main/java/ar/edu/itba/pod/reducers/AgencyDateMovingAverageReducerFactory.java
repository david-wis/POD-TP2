package ar.edu.itba.pod.reducers;

import ar.edu.itba.pod.models.AgencyDate;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;

@SuppressWarnings("deprecation")
public class AgencyDateMovingAverageReducerFactory implements ReducerFactory<AgencyDate, Long, BigDecimal> {

    private final int windowSize;

    public AgencyDateMovingAverageReducerFactory(int w) {
        this.windowSize = w;
    }

    @Override
    public Reducer<Long, BigDecimal> newReducer(AgencyDate agencyDate) {
        return new AgencyDateMovingAverageReducer(windowSize);
    }

    private static class AgencyDateMovingAverageReducer extends Reducer<Long, BigDecimal> {
        private final int windowSize;
        private long total;

        public AgencyDateMovingAverageReducer(int windowSize) {
            this.windowSize = windowSize;
        }

        @Override
        public void beginReduce() {
            total = 0;
        }

        @Override
        public void reduce(Long value) {
            total += value;
        }

        @Override
        public BigDecimal finalizeReduce() {
            return BigDecimal.valueOf(total).divide(BigDecimal.valueOf(windowSize), 2, RoundingMode.DOWN);
        }
    }
}
