package ar.edu.itba.pod.client;

import ar.edu.itba.pod.models.Complaint;
import com.hazelcast.core.IMap;

import java.time.YearMonth;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import static ar.edu.itba.pod.client.Constants.*;


public class ComplaintMappers {

    private static final Function<String[], Complaint> newYorkComplaintMapper = s -> {
        Complaint.ComplaintBuilder builder = new Complaint.ComplaintBuilder();
        builder.setId(s[NYC_ID])
                .setNeighborhood(s[NYC_BOROUGH])
                .setLatitude(Double.parseDouble(s[NYC_LATITUDE]))
                .setLongitude(Double.parseDouble(s[NYC_LONGITUDE]))
                .setDate(YearMonth.parse(s[NYC_CREATED_DATE].substring(0, 7)))
                .setStreet(s[NYC_INCIDENT_ADDRESS].replaceFirst("^\\d+ ", ""))
                .setType(s[NYC_COMPLAINT_TYPE])
                .setOpen(!s[NYC_STATUS].equals("Closed"))
                .setAgency(s[NYC_AGENCY_NAME]);
        return builder.build();
    };

    private static final Function<String[], Complaint> chicagoComplaintMapper = s -> {
        Complaint.ComplaintBuilder builder = new Complaint.ComplaintBuilder();
        builder.setId(s[CHI_SR_NUMBER])
                .setNeighborhood(s[CHI_COMMUNITY_AREA])
                .setLatitude(Double.parseDouble(s[CHI_LATITUDE]))
                .setLongitude(Double.parseDouble(s[CHI_LONGITUDE]))
                .setDate(YearMonth.parse(s[CHI_CREATED_DATE].substring(0, 7)))
                .setStreet(s[CHI_STREET_DIRECTION] + " " + s[CHI_STREET_NAME] + " " + s[CHI_STREET_TYPE])
                .setType(s[CHI_SR_SHORT_CODE])
                .setOpen(s[CHI_STATUS].equals("Open"))
                .setAgency(s[CHI_OWNER_DEPARTMENT]);
        return builder.build();
    };

    private static final Function<IMap<String, String>, Consumer<String[]>> chicagoComplaintTypeConsumer = typeMap -> t -> {
        typeMap.put(t[0], t[1]);
    };

    private static final Function<IMap<String, String>, Consumer<String[]>> nycComplaintTypeConsumer = typeMap -> t -> {
        typeMap.put(t[0], t[0]);
    };

    public static final Map<String, Function<String[], Complaint>> mappers = Map.of(
            NYC_CITY, newYorkComplaintMapper,
            CHI_CITY, chicagoComplaintMapper
    );

    public static final Map<String, Function<IMap<String, String>, Consumer<String[]>>> complaintTypeConsumers = Map.of(
            NYC_CITY, nycComplaintTypeConsumer,
            CHI_CITY, chicagoComplaintTypeConsumer
    );
}
