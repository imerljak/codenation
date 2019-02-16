package challenge;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class RedisRepositoryImpl<T> implements RedisRepository<T> {

    private final RedisTemplate<String, T> template;

    public RedisRepositoryImpl(RedisTemplate<String, T> redisTemplate) {
        this.template = redisTemplate;
    }

    @Override
    public T put(String key, T object) {
        template.opsForValue().set(key, object);
        return object;
    }

    @Override
    public Optional<T> findByKey(String id) {
        return Optional.ofNullable(template.opsForValue().get(id));
    }
}
