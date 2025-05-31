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

    // Available cities
    public static final String NYC_CITY = "NYC";
    public static final String CHI_CITY = "CHI";

    // Default values
    public static final String USERNAME = "g4user";
    public static final String PASSWORD = "g4pass";

    // CSV Headers
    public static final String QUERY1_HEADERS = "type;agency;requests";
    public static final String QUERY2_HEADERS = "neighbourhood;quadLat;quadLong;topType";
    public static final String QUERY3_HEADERS = "agency;year;month;movingAverage";
    public static final String QUERY4_HEADERS = "street;typePercentage";

    // Files
    public static final String QUERY_FILE_TEMPLATE = "query%d.txt";
    public static final String TIME_FILE_TEMPLATE = "time%d.txt";
    public static final String SERVICE_REQUESTS_FILE_TEMPLATE = "serviceRequests%s.csv";
    public static final String SERVICE_TYPES_FILE_TEMPLATE = "serviceTypes%s.csv";


}
