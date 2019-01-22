package challenge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    private final RedisOperations<String, NeighborhoodRedis> redisRedisOperations;
    private final MongoOperations mongoOperations;

    @Autowired
    public RestaurantServiceImpl(RedisOperations<String, NeighborhoodRedis> redisRedisOperations, MongoOperations mongoOperations) {
        this.redisRedisOperations = redisRedisOperations;
        this.mongoOperations = mongoOperations;
    }

    @Override
    public NeighborhoodRedis findInNeighborhood(double x, double y) {
        final NeighborhoodMongo neighborhood = findNeighborhood(x, y);

        String redisKey = "neighborhood:" + neighborhood.getId();
        NeighborhoodRedis neighborhoodRedis = redisRedisOperations.opsForValue().get(redisKey);

        if (neighborhoodRedis == null) {

            neighborhoodRedis = NeighborhoodRedis.from(neighborhood);

            neighborhoodRedis.setRestaurants(
                    findAllInNeighborhood(neighborhood)
                            .stream()
                            .sequential()
                            .map(this::restaurantMongoToRedis)
                            .collect(Collectors.toList()));

            redisRedisOperations.opsForValue().set(redisKey, neighborhoodRedis);
        }

        return neighborhoodRedis;
    }

    private RestaurantRedis restaurantMongoToRedis(RestaurantMongo restaurantMongo) {
        final RestaurantRedis restaurantRedis = new RestaurantRedis();
        restaurantRedis.setId(restaurantMongo.getId());
        restaurantRedis.setName(restaurantMongo.getName());
        restaurantRedis.setX(restaurantMongo.getLocation().getX());
        restaurantRedis.setY(restaurantMongo.getLocation().getY());

        return restaurantRedis;
    }

    private NeighborhoodMongo findNeighborhood(double x, double y) {
        return mongoOperations.findOne(
                query(where("geometry").intersects(new GeoJsonPoint(new Point(x, y)))),
                NeighborhoodMongo.class);
    }

    private List<RestaurantMongo> findAllInNeighborhood(NeighborhoodMongo neighborhoodMongo) {
        return mongoOperations.find(
                query(where("location").within(neighborhoodMongo.getGeometry()))
                .with(Sort.by("name").ascending()),
                RestaurantMongo.class
        );
    }

}
