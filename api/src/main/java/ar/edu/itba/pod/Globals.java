package ar.edu.itba.pod;

public class Globals {
    public static final String COMPLAINTS_MAP_NAME = "g4-complaints";
    public static final String TYPES_MAP_NAME = "g4-types";

    public static final String USERNAME = "g4user";
    public static final String PASSWORD = "g4pass";

    // Jobs
    public static final String TOTAL_COMPLAINTS_BY_TYPE_AGENCY_JOB_TRACKER_NAME = "g4-TotalComplaintsByTypeAgency";
    public static final String MOST_POPULAR_TYPE_JOB_TRACKER_NAME = "g4-MostPopularType";
    public static final String AGENCY_DATE_MOVING_AVERAGE_JOB_TRACKER_NAME = "g4-AgencyDateMovingAverage";
    public static final String TOTAL_TYPE_COUNT_JOB_TRACKER_NAME = "g4-TotalTypeCount";

    // Distributed collections
    public static final String NEIGHBOURHOOD_TYPE_COUNT_MAP_NAME = "g4-neighborhoodQuadTypeCountMap";
    public static final String TYPE_STREET_MAP_NAME = "g4-TypeStreetMap";

    // System properties (keys)
    public static final String PROPERTY_INTERFACES = "interfaces";
    public static final String PROPERTY_PUBLIC_ADDRESS = "publicAddress";
    public static final String PROPERTY_MODE = "mode";
    public static final String PROPERTY_MEMBERS = "members";

    // Default values
    public static final String DEFAULT_INTERFACE = "127.0.0.*";
    public static final String DEFAULT_MODE = "multicast";

}
