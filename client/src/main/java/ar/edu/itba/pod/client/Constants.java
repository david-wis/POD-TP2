package ar.edu.itba.pod.client;

public class Constants {
    // Argument keys
    public static final String ADDRESSES_ARG = "addresses";
    public static final String CITY_ARG = "city";
    public static final String IN_PATH_ARG = "inPath";
    public static final String OUT_PATH_ARG = "outPath";
    public static final String Q_ARG = "q";
    public static final String W_ARG = "w";
    public static final String NEIGHBOURHOOD_ARG = "neighbourhood";

    public static final float Q_MIN = 0.0f;
    public static final float Q_MAX = 1.0f;

    public static final int W_MIN = 1;
    public static final int W_MAX = 12;

    // Available cities
    public static final String NYC_CITY = "NYC";
    public static final String CHI_CITY = "CHI";

    // Batch size
    public static final int COMPLAINT_BATCH_SIZE = 100_000;

    // CSV Headers
    public static final String QUERY1_HEADERS = "type;agency;requests";
    public static final String QUERY2_HEADERS = "neighbourhood;quadLat;quadLon;topType";
    public static final String QUERY3_HEADERS = "agency;year;month;movingAvg";
    public static final String QUERY4_HEADERS = "street;typePercentage";

    // Files
    public static final String QUERY_FILE_TEMPLATE = "query%d.csv";
    public static final String TIME_FILE_TEMPLATE = "time%d.txt";
    public static final String SERVICE_REQUESTS_FILE_TEMPLATE = "serviceRequests%s.csv";
    public static final String SERVICE_TYPES_FILE_TEMPLATE = "serviceTypes%s.csv";

    // Logger
    public static final String CUSTOM_LOGGER_NAME = "CustomLogger";

    // ------------------ NYC ------------------
    public static final int NYC_ID = 0;
    public static final int NYC_CREATED_DATE = 1;
    public static final int NYC_AGENCY_NAME = 2;
    public static final int NYC_COMPLAINT_TYPE = 3;
    public static final int NYC_INCIDENT_ADDRESS = 4;
    public static final int NYC_STATUS = 5;
    public static final int NYC_BOROUGH = 6;
    public static final int NYC_LATITUDE = 7;
    public static final int NYC_LONGITUDE = 8;

    // ------------------ CHICAGO ------------------
    public static final int CHI_SR_NUMBER = 0;
    public static final int CHI_SR_SHORT_CODE = 1;
    public static final int CHI_OWNER_DEPARTMENT = 2;
    public static final int CHI_STATUS = 3;
    public static final int CHI_CREATED_DATE = 4;
    public static final int CHI_STREET_NUMBER = 5;
    public static final int CHI_STREET_DIRECTION = 6;
    public static final int CHI_STREET_NAME = 7;
    public static final int CHI_STREET_TYPE = 8;
    public static final int CHI_COMMUNITY_AREA = 9;
    public static final int CHI_LATITUDE = 10;
    public static final int CHI_LONGITUDE = 11;

}
