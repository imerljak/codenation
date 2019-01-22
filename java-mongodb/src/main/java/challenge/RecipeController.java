package challenge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RecipeController {

    private final RecipeService service;

    @Autowired
    public RecipeController(RecipeService service) {this.service = service;}

    @PostMapping(value = "/recipe")
    public Recipe save(@RequestBody Recipe recipe) {
        return service.save(recipe);
    }

    @PutMapping("/recipe/{id}")
    public void update(@PathVariable String id, @RequestBody Recipe recipe) {
        service.update(id, recipe);
    }

    @DeleteMapping("/recipe/{id}")
    public void delete(@PathVariable String id) {
        service.delete(id);
    }

    @GetMapping("/recipe/{id}")
    public Recipe get(@PathVariable String id) {
        return service.get(id);
    }

    @GetMapping(value = "/recipe/ingredient")
    public List<Recipe> listByIngredient(@RequestParam String ingredient) { return service.listByIngredient(ingredient); }

    @GetMapping("/recipe/search")
    public List<Recipe> search(@RequestParam String search) {
        return service.search(search);
    }

    @PostMapping("/recipe/{id}/like/{userId}")
    public void like(@PathVariable String id, @PathVariable String userId) {
        service.like(id, userId);
    }

    @DeleteMapping("/recipe/{id}/like/{userId}")
    public void unlike(@PathVariable String id, @PathVariable String userId) {
        service.unlike(id, userId);
    }

    @PostMapping("/recipe/{id}/comment")
    public RecipeComment addComment(@PathVariable String id, @RequestBody RecipeComment recipeComment) {
        return service.addComment(id, recipeComment);
    }

    @PutMapping("/recipe/{id}/comment/{commentId}")
    public void updateComment(@PathVariable String id, @PathVariable String commentId, @RequestBody RecipeComment recipeComment) {
        service.updateComment(id, commentId, recipeComment);
    }

    @DeleteMapping("/recipe/{id}/comment/{commentId}")
    public void deleteComment(@PathVariable String id, @PathVariable String commentId) {
        service.deleteComment(id, commentId);
    }

}
