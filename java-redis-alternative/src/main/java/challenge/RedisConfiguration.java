package challenge;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableRedisRepositories
public class RedisConfiguration {

    @Primary
    @Bean
    public RedisTemplate<String, NeighborhoodRedis> neighborhoodRedisRedisTemplate(RedisConnectionFactory connectionFactory) {
        return redisTemplateBuilder(connectionFactory, NeighborhoodRedis.class);
    }

    private <T> RedisTemplate<String, T> redisTemplateBuilder(RedisConnectionFactory connectionFactory, Class<T> tClass) {
        final RedisTemplate<String, T> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(tClass));
        template.setKeySerializer(new StringRedisSerializer());
        return template;
    }

}
