package ar.edu.itba.pod.client;

import ar.edu.itba.pod.models.Complaint;

import java.time.YearMonth;
import java.util.Map;
import java.util.function.Function;

import static ar.edu.itba.pod.client.Constants.CHI_CITY;
import static ar.edu.itba.pod.client.Constants.NYC_CITY;

public class ComplaintMappers {
    private static final Function<String[], Complaint> newYorkComplaintMapper = s -> {
        Complaint.ComplaintBuilder builder = new Complaint.ComplaintBuilder();
        builder.setId(s[0])
                .setNeighborhood(s[6])
                .setLatitude(Float.parseFloat(s[7]))
                .setLongitude(Float.parseFloat(s[8]))
                .setDate(YearMonth.parse(s[1].substring(0, 7)))
                .setStreet(s[4].replaceFirst("^\\d+ ", ""))
                .setType(s[3])
                .setOpen(!s[5].equals("Closed"))
                .setAgency(s[2]);
        return builder.build();
    };

    private static final Function<String[], Complaint> chicagoComplaintMapper = s -> {
        Complaint.ComplaintBuilder builder = new Complaint.ComplaintBuilder();
        builder.setId(s[0])
                .setNeighborhood(s[9])
                .setLatitude(Double.parseDouble(s[10]))
                .setLongitude(Double.parseDouble(s[11]))
                .setDate(YearMonth.parse(s[4].substring(0, 7)))
                .setStreet(s[6] + s[7] + s[8])
                .setType(s[1])
                .setOpen(s[3].equals("Open"))
                .setAgency(s[2]);
        return builder.build();
    };

    public static Map<String, Function<String[], Complaint>> mappers = Map.of(
            NYC_CITY, newYorkComplaintMapper,
            CHI_CITY, chicagoComplaintMapper
    );
}
