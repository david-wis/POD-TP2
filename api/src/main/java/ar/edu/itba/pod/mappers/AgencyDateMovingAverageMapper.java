package ar.edu.itba.pod.mappers;

import ar.edu.itba.pod.models.AgencyDate;
import ar.edu.itba.pod.models.Complaint;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

@SuppressWarnings("deprecation")
public class AgencyDateMovingAverageMapper implements Mapper<String, Complaint, AgencyDate, Long> {
    private final int windowSize;

    public AgencyDateMovingAverageMapper(int w) {
        this.windowSize = w;
    }

    @Override
    public void map(String id, Complaint complaint, Context<AgencyDate, Long> context) {
        if (!complaint.isOpen())
            return; // Only consider open complaints

        int month = complaint.getMonth();
        int end = Math.min(month + windowSize - 1, 12);
        for (int i = month; i <= end; i++) {
            AgencyDate newAgencyDate = new AgencyDate(complaint, month);
            context.emit(newAgencyDate, 1L);
        }
    }
}
