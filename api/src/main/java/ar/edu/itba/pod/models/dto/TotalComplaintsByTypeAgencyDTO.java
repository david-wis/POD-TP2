package ar.edu.itba.pod.models.dto;

public class TotalComplaintsByTypeAgencyDTO {
    private final String type;
    private final String agency;
    private final int total;

    public TotalComplaintsByTypeAgencyDTO(final String type, final String agency, final int total) {
        this.type = type;
        this.agency = agency;
        this.total = total;
    }

    public String getType() {
        return type;
    }
    public String getAgency() {
        return agency;
    }
    public int getTotal() {
        return total;
    }
}
