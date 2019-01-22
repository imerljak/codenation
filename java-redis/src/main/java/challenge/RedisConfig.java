package challenge;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory();
    }

    @Bean
    public RedisTemplate<String, NeighborhoodRedis> neighborhoodRedisTemplate(RedisConnectionFactory connectionFactory) {
        return redisTemplateBuilder(connectionFactory, NeighborhoodRedis.class);
    }

    @Bean
    public RedisTemplate<String, RestaurantRedis> restaurantRedisTemplate(RedisConnectionFactory connectionFactory) {
        return redisTemplateBuilder(connectionFactory, RestaurantRedis.class);
    }

    private <T> RedisTemplate<String, T> redisTemplateBuilder(RedisConnectionFactory connectionFactory, Class<T> tClass) {
        final RedisTemplate<String, T> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(tClass));
        template.setKeySerializer(new StringRedisSerializer());
        return template;
    }

}
