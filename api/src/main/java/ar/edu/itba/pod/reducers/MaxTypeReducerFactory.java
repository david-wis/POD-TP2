package ar.edu.itba.pod.reducers;

import ar.edu.itba.pod.models.NeighborhoodQuad;
import ar.edu.itba.pod.models.TypeCount;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;
@SuppressWarnings("deprecation")
public class MaxTypeReducerFactory implements ReducerFactory<NeighborhoodQuad, TypeCount, TypeCount> {

    @Override
    public Reducer<TypeCount, TypeCount> newReducer(NeighborhoodQuad neighborhoodQuad) {
        return new MaxTypeReducer();
    }

    private static class MaxTypeReducer extends Reducer<TypeCount, TypeCount> {
        private TypeCount max;

        @Override
        public void beginReduce() {
            max = null;
        }

        @Override
        public void reduce(TypeCount value) {
            if (max == null || value.getCount() > max.getCount()) {
                max = value;
            }
        }

        @Override
        public TypeCount finalizeReduce() {
            return max;
        }
    }
}
