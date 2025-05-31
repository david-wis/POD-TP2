package ar.edu.itba.pod.collators;

import ar.edu.itba.pod.models.TypeAgency;
import ar.edu.itba.pod.models.dto.TotalComplaintsByTypeAgencyDTO;
import com.hazelcast.mapreduce.Collator;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@SuppressWarnings("deprecation")
public class TotalComplaintsByTypeAgencyCollator implements Collator<Map.Entry<TypeAgency, Integer>, List<TotalComplaintsByTypeAgencyDTO>> {
    @Override
    public List<TotalComplaintsByTypeAgencyDTO> collate(Iterable<Map.Entry<TypeAgency, Integer>> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false)
                .map(entry -> new TotalComplaintsByTypeAgencyDTO(
                        entry.getKey().getType(),
                        entry.getKey().getAgency(),
                        entry.getValue()))
                .sorted(Comparator.comparing(TotalComplaintsByTypeAgencyDTO::total).reversed()
                        .thenComparing(TotalComplaintsByTypeAgencyDTO::type)
                        .thenComparing(TotalComplaintsByTypeAgencyDTO::agency))
                .collect(Collectors.toList());
    }
}