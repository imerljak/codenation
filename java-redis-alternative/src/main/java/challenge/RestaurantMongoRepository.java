package challenge;

import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantMongoRepository extends MongoRepository<RestaurantMongo, String> {

    List<RestaurantMongo> findAllByLocationIsWithinOrderByNameAsc(GeoJsonPolygon area);

}
