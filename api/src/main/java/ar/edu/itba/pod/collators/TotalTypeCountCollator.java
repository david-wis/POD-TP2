package ar.edu.itba.pod.collators;

import com.hazelcast.mapreduce.Collator;

import java.util.Map;
import java.util.stream.StreamSupport;

@SuppressWarnings("deprecation")
public class TotalTypeCountCollator implements Collator<Map.Entry<String, Boolean>,Long> {
    @Override
    public Long collate(Iterable<Map.Entry<String, Boolean>> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false).count();
    }
}
