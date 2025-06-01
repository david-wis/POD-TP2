package ar.edu.itba.pod.collators;

import ar.edu.itba.pod.models.NeighborhoodQuad;
import ar.edu.itba.pod.models.TypeAgency;
import ar.edu.itba.pod.models.TypeCount;
import ar.edu.itba.pod.models.dto.NeighborhoodQuadTypeMaxCountDTO;
import com.hazelcast.mapreduce.Collator;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@SuppressWarnings("deprecation")
public class NeighborhoodQuadTypeMaxCountCollator implements Collator<Map.Entry<NeighborhoodQuad, TypeCount>, List<NeighborhoodQuadTypeMaxCountDTO>> {
    @Override
    public List<NeighborhoodQuadTypeMaxCountDTO> collate(Iterable<Map.Entry<NeighborhoodQuad, TypeCount>> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false)
                .map(entry -> new NeighborhoodQuadTypeMaxCountDTO(
                        entry.getKey().getNeighborhood(),
                        entry.getKey().getQuadLat(),
                        entry.getKey().getQuadLon(),
                        entry.getValue().getType()))
                .sorted(Comparator.comparing(NeighborhoodQuadTypeMaxCountDTO::neighborhood)
                        .thenComparing(NeighborhoodQuadTypeMaxCountDTO::quadLat)
                        .thenComparing(NeighborhoodQuadTypeMaxCountDTO::quadLon))
                .collect(Collectors.toList());
    }
}