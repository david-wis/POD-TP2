package ar.edu.itba.pod.collators;

import ar.edu.itba.pod.models.dto.TypePercentageByStreetDTO;
import com.hazelcast.mapreduce.Collator;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@SuppressWarnings("deprecation")
public class TypePercentageByStreetCollator implements Collator<Map.Entry<String, Double>, List<TypePercentageByStreetDTO>> {
    @Override
    public List<TypePercentageByStreetDTO> collate(Iterable<Map.Entry<String, Double>> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false)
                .map(entry -> new TypePercentageByStreetDTO(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparing(TypePercentageByStreetDTO::percentage).reversed()
                        .thenComparing(TypePercentageByStreetDTO::street))
                .collect(Collectors.toList());    }
}
