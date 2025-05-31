package ar.edu.itba.pod.mappers;

import ar.edu.itba.pod.models.Complaint;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

@SuppressWarnings("deprecation")
public class TotalTypeCountMapper implements Mapper<String, Complaint, String, Boolean> {
    @Override
    public void map(String s, Complaint complaint, Context<String, Boolean> context) {
        context.emit(complaint.getStreet(), true);
    }
}
