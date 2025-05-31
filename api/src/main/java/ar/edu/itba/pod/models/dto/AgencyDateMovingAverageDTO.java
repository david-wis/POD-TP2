package ar.edu.itba.pod.models.dto;

import java.math.BigDecimal;

public record AgencyDateMovingAverageDTO(
        String agency,
        int year,
        int month,
        BigDecimal movingAverage
) {

}
