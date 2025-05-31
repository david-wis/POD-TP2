package ar.edu.itba.pod.reducers;

import ar.edu.itba.pod.models.TypeStreet;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

@SuppressWarnings("deprecation")
public class TypeStreetUniqueReducerFactory implements ReducerFactory<TypeStreet, Integer, Integer> {
    @Override
    public Reducer<Integer, Integer> newReducer(TypeStreet typeStreet) {
        return new TypeStreetUniqueReducer();
    }

    private static class TypeStreetUniqueReducer extends Reducer<Integer, Integer> {
        private int exists = 0;

        @Override
        public void beginReduce() {
            exists = 0;
        }

        @Override
        public void reduce(Integer value) {
            exists = Math.max(exists, value);
        }

        @Override
        public Integer finalizeReduce() {
            return exists;
        }
    }
}
