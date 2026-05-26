package ec.insti.medista.security.internal.service.impl.ratelimit;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;

@Component
public class LoginRateLimiter {

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();
    
    private Bucket createNewBucket() {

        Bandwidth limit = Bandwidth.builder()
                .capacity(5)
                .refillGreedy(5, Duration.ofMinutes(5))
            .build();

        return Bucket.builder()
                .addLimit(limit)
            .build();    
        
    }

    public boolean isAllowed(String ipAddress) {

        Bucket bucket = buckets.computeIfAbsent(ipAddress, k -> createNewBucket());

        return bucket.tryConsume(1);

    }

}
