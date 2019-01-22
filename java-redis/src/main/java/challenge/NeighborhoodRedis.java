package challenge;


import java.util.List;
import java.util.Objects;

/**
 * Classe para mapear o bairro no Redis
 *
 */
public class NeighborhoodRedis {

    private String id;
    private String name;
    private List<RestaurantRedis> restaurants;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<RestaurantRedis> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(List<RestaurantRedis> restaurants) {
        this.restaurants = restaurants;
    }

    public static NeighborhoodRedis from(NeighborhoodMongo neighborhoodMongo) {
        final NeighborhoodRedis neighborhoodRedis = new NeighborhoodRedis();
        neighborhoodRedis.setId(neighborhoodMongo.getId());
        neighborhoodRedis.setName(neighborhoodMongo.getName());

        return neighborhoodRedis;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NeighborhoodRedis)) return false;
        NeighborhoodRedis that = (NeighborhoodRedis) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
