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
        return new AgencyDateMovingAverageReducer(agencyDate, windowSize);
    }

    private static class AgencyDateMovingAverageReducer extends Reducer<Long, BigDecimal> {
        private final AgencyDate agencyDate;
        private final int windowSize;
        private long total;

        public AgencyDateMovingAverageReducer(AgencyDate agencyDate, int windowSize) {
            this.agencyDate = agencyDate;
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
            int month = agencyDate.getMonth();
            return BigDecimal.valueOf(total).divide(BigDecimal.valueOf(Math.min(windowSize, month)), 2, RoundingMode.DOWN);
        }
    }
}
