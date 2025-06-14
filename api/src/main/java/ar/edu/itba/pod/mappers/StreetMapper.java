package ar.edu.itba.pod.mappers;

import ar.edu.itba.pod.models.TypeStreet;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

@SuppressWarnings("deprecation")
public class StreetMapper implements Mapper<TypeStreet, Integer, String, Long> {
    @Override
    public void map(TypeStreet key, Integer value, Context<String, Long> context) {
        context.emit(key.getStreet(), (long) value);
    }
}
