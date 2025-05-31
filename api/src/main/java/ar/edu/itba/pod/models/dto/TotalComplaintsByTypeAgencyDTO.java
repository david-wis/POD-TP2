package ar.edu.itba.pod.models.dto;

public record TotalComplaintsByTypeAgencyDTO(
        String type,
        String agency,
        long total
) {
}
