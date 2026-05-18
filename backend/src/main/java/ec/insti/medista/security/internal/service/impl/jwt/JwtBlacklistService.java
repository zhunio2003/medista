package ec.insti.medista.security.internal.service.impl.jwt;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtBlacklistService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;

    public void blacklistToken(String token) {
        redisTemplate.opsForValue().set(
            "blacklist:" + token,
            "blacklisted",
            jwtTokenProvider.extractExpiration(token).getTime() - System.currentTimeMillis(),
            TimeUnit.MILLISECONDS
        );
    }

    public boolean isBlacklisted(String token) {
        return redisTemplate.hasKey("blacklist:" + token);
    }
}
