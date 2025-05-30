package ar.edu.itba.pod.collators;

import ar.edu.itba.pod.models.TypeAgency;
import ar.edu.itba.pod.models.dto.TotalComplaintsByTypeAgencyDTO;
import com.hazelcast.mapreduce.Collator;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class TotalComplaintsByTypeAgencyCollator implements Collator<Map.Entry<TypeAgency, Integer>, List<TotalComplaintsByTypeAgencyDTO>> {
    @Override
    public List<TotalComplaintsByTypeAgencyDTO> collate(Iterable<Map.Entry<TypeAgency, Integer>> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false)
                .map(entry -> new TotalComplaintsByTypeAgencyDTO(
                        entry.getKey().getType(),
                        entry.getKey().getAgency(),
                        entry.getValue()))
                // sort by value then type then agency
                .sorted((dto1, dto2) -> {
                    int cmp = Integer.compare(dto2.getTotal(), dto1.getTotal());
                    if (cmp != 0) return cmp;
                    cmp = dto1.getType().compareTo(dto2.getType());
                    if (cmp != 0) return cmp;
                    return dto1.getAgency().compareTo(dto2.getAgency());
                })
                .collect(Collectors.toList());
    }
}
