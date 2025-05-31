package ar.edu.itba.pod.mappers;

import ar.edu.itba.pod.models.Complaint;
import ar.edu.itba.pod.models.TypeStreet;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

@SuppressWarnings("deprecation")
public class TypeStreetMapper implements Mapper<String, Complaint, TypeStreet, Integer> {
    @Override
    public void map(String key, Complaint complaint, Context<TypeStreet, Integer> context) {
        TypeStreet typeStreet = new TypeStreet(complaint);
        context.emit(typeStreet, 1);
    }
}
