package ar.edu.itba.pod.collators;

import ar.edu.itba.pod.models.AgencyDate;
import ar.edu.itba.pod.models.dto.AgencyDateMovingAverageDTO;
import com.hazelcast.mapreduce.Collator;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@SuppressWarnings("deprecation")
public class AgencyDateMovingAverageCollator implements Collator<Map.Entry<AgencyDate, BigDecimal>, List<AgencyDateMovingAverageDTO>> {

    @Override
    public List<AgencyDateMovingAverageDTO> collate(Iterable<Map.Entry<AgencyDate, BigDecimal>> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false)
                .map(entry -> new AgencyDateMovingAverageDTO(
                        entry.getKey().getAgency(),
                        entry.getKey().getYear(),
                        entry.getKey().getMonth(),
                        entry.getValue()))
                .sorted(Comparator.comparing(AgencyDateMovingAverageDTO::agency)
                                  .thenComparingInt(AgencyDateMovingAverageDTO::year)
                                  .thenComparingInt(AgencyDateMovingAverageDTO::month))
                .collect(Collectors.toList());
    }
}
