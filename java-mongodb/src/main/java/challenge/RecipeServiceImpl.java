package challenge;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class RecipeServiceImpl implements RecipeService {


    private final MongoOperations mongoOperations;

    @Autowired
    public RecipeServiceImpl(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    @Override
    public Recipe save(Recipe recipe) {
        mongoOperations.insert(recipe);
        return recipe;
    }

    @Override
    public void update(String id, Recipe newRecipe) {
        mongoOperations.updateFirst(
                query(where("_id").is(id)),

                Update.update("title", newRecipe.getTitle())
                        .set("description", newRecipe.getDescription())
                        .set("ingredients", newRecipe.getIngredients()),

                Recipe.class);
    }

    @Override
    public void delete(String id) {
        mongoOperations.remove(query(where("_id").is(id)), Recipe.class);
    }

    @Override
    public Recipe get(String id) {
        return mongoOperations.findById(id, Recipe.class);
    }

    @Override
    public List<Recipe> listByIngredient(String ingredient) {

        return mongoOperations.find(
                query(where("ingredients").in(ingredient)).with(Sort.by("title").ascending()),
                Recipe.class
        );

    }

    @Override
    public List<Recipe> search(String search) {

        //        search = ".*" + search + ".*";

        final Pattern pattern = Pattern.compile(search, Pattern.CASE_INSENSITIVE);

        return mongoOperations.find(
                query(new Criteria().orOperator(
                        where("title").regex(pattern),
                        where("description").regex(pattern))
                ).with(Sort.by("title").ascending()),

                Recipe.class
        );
    }

    @Override
    public void like(String id, String userId) {

        mongoOperations.updateFirst(
                query(where("_id").is(id)),

                new Update().addToSet("likes", userId),

                Recipe.class
        );

    }

    @Override
    public void unlike(String id, String userId) {

        mongoOperations.updateFirst(
                query(where("_id").is(id)),

                new Update().pull("likes", userId),

                Recipe.class
        );

    }

    @Override
    public RecipeComment addComment(String id, RecipeComment comment) {

        comment.setId(ObjectId.get().toHexString());

        mongoOperations.updateFirst(
                query(where("_id").is(id)),
                new Update().addToSet("comments", comment),
                Recipe.class
        );

        return comment;
    }

    @Override
    public void updateComment(String id, String commentId, RecipeComment comment) {

        mongoOperations.updateFirst(
                query(where("_id").is(id).and("comments._id").is(commentId)),
                Update.update("comments.$.comment", comment.getComment()),
                Recipe.class
        );

    }

    @Override
    public void deleteComment(String id, String commentId) {

        mongoOperations.updateFirst(
                query(where("_id").is(id)),
                new Update().pull("comments", query(where("_id").is(commentId))),
                Recipe.class
        );
    }

}
