package no.nav.kafka;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class PoststedCounter {

    private final HashMap<String, Integer> count = new HashMap<>();


    public void count(final Poststed poststed) {
        if (poststed.getStedsnavn().startsWith("K")) {
            throw new RuntimeException("Lagring av poststed feilet! --> " + poststed);
        }
        final Integer currentCount = count.getOrDefault(poststed.getStedsnavn(), 0);
        count.put(poststed.getStedsnavn(), currentCount + 1);
    }


    public Map<String, Integer> getTopThreeCount() {
        return Collections.unmodifiableMap(
            count
                .entrySet()
                .stream()
                .sorted((Map.Entry.<String, Integer>comparingByValue().reversed()))
                .limit(3)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new))
        );
    }

}
