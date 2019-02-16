package challenge;

import java.util.Optional;

public interface RedisRepository<T> {

    T put(String key, T object);

    Optional<T> findByKey(String id);
}
