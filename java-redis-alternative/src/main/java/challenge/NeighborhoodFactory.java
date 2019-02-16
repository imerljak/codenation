package challenge;

public class NeighborhoodFactory {
    public static NeighborhoodRedis getInstance(NeighborhoodMongo neighborhoodMongo) {

        final NeighborhoodRedis neighborhoodRedis = new NeighborhoodRedis();
        neighborhoodRedis.setId(neighborhoodMongo.getId());
        neighborhoodRedis.setName(neighborhoodMongo.getName());
        return neighborhoodRedis;
    }
}
