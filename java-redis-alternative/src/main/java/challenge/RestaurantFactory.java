package challenge;

public class RestaurantFactory {
    public static RestaurantRedis getInstance(RestaurantMongo restaurantMongo) {

        final RestaurantRedis restaurantRedis = new RestaurantRedis();
        restaurantRedis.setId(restaurantMongo.getId());
        restaurantRedis.setName(restaurantMongo.getName());
        restaurantRedis.setX(restaurantMongo.getLocation().getX());
        restaurantRedis.setY(restaurantMongo.getLocation().getY());

        return restaurantRedis;
    }
}
