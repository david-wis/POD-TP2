package ar.edu.itba.pod.mappers;

import ar.edu.itba.pod.models.TypeStreet;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

@SuppressWarnings("deprecation")
public class StreetMapper implements Mapper<TypeStreet, Integer, String, Integer> {
    @Override
    public void map(TypeStreet key, Integer value, Context<String, Integer> context) {
        context.emit(key.getStreet(), value);
    }
}
