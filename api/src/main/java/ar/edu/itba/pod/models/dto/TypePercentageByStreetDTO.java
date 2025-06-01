package ar.edu.itba.pod.models.dto;

import java.math.BigDecimal;

public record TypePercentageByStreetDTO (
        String street, BigDecimal percentage
) {
}
