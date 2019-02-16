package challenge;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Repository;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Repository
public class NeighborhoodMongoRepositoryImpl implements NeighborhoodMongoRepository {

    private final MongoTemplate mongoTemplate;

    public NeighborhoodMongoRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public NeighborhoodMongo findByGeometryIntersects(GeoJsonPoint point) {
        return mongoTemplate.findOne(query(where("geometry").intersects(point)), NeighborhoodMongo.class);
    }
}
