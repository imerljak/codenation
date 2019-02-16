package challenge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    private final Logger logger = LoggerFactory.getLogger(RestaurantServiceImpl.class);

    private final RestaurantMongoRepository restaurantMongoRepository;
    private final NeighborhoodMongoRepository neighborhoodMongoRepository;
    private final RedisRepository<NeighborhoodRedis> redisRepository;

    public RestaurantServiceImpl(RestaurantMongoRepository restaurantMongoRepository,
                                 NeighborhoodMongoRepository neighborhoodMongoRepository,
                                 RedisRepository<NeighborhoodRedis> redisRepository) {
        this.restaurantMongoRepository = restaurantMongoRepository;
        this.neighborhoodMongoRepository = neighborhoodMongoRepository;
        this.redisRepository = redisRepository;
    }

    @Override
    public NeighborhoodRedis findInNeighborhood(double x, double y) {
        final NeighborhoodMongo neighborhoodMongo = getNeighborhoodMongo(x, y);

        logger.info("Found from mongo: {}", neighborhoodMongo.getId());
        logger.info("Getting neighborhood data.");

            return redisRepository.findByKey("neighborhood:" + neighborhoodMongo.getId())
                .orElseGet(() -> {
                    logger.info("Data not found in redis.. fetching from mongo.");
                    return getNeighborhoodRedis(neighborhoodMongo);
                });
    }

    private NeighborhoodMongo getNeighborhoodMongo(double x, double y) {
        final GeoJsonPoint point = new GeoJsonPoint(new Point(x, y));
        return neighborhoodMongoRepository.findByGeometryIntersects(point);
    }

    private NeighborhoodRedis getNeighborhoodRedis(NeighborhoodMongo neighborhoodMongo) {
        final NeighborhoodRedis neighborhoodRedis = NeighborhoodFactory.getInstance(neighborhoodMongo);
        neighborhoodRedis.setRestaurants(getRestaurants(neighborhoodMongo));

        return redisRepository.put("neighborhood:" + neighborhoodRedis.getId(), neighborhoodRedis);
    }

    private List<RestaurantRedis> getRestaurants(NeighborhoodMongo neighborhoodMongo) {
        return restaurantMongoRepository
                .findAllByLocationIsWithinOrderByNameAsc(neighborhoodMongo.getGeometry())
                .stream()
                .sequential()
                .map(RestaurantFactory::getInstance)
                .collect(Collectors.toList());
    }

}
