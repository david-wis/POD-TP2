package ar.edu.itba.pod.mappers;

import ar.edu.itba.pod.models.Complaint;
import ar.edu.itba.pod.models.TypeAgency;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

// TODO: Use generics?
public class TotalComplaintsByTypeAgencyMapper implements Mapper<String, Complaint, TypeAgency, Integer> {

    @Override
    public void map(String s, Complaint complaint, Context<TypeAgency, Integer> context) {
        final TypeAgency typeAgency = new TypeAgency(complaint);
        context.emit(typeAgency, 1);
    }
}
