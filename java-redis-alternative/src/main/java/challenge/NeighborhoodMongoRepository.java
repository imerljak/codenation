package challenge;

import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

public interface NeighborhoodMongoRepository {

    NeighborhoodMongo findByGeometryIntersects(GeoJsonPoint point);

}
