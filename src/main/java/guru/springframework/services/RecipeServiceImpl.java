package guru.springframework.services;

import guru.springframework.command.RecipeCommand;
import guru.springframework.converters.RecipeCommandToRecipe;
import guru.springframework.converters.RecipeToRecipeCommand;
import guru.springframework.domain.Recipe;
import guru.springframework.exception.NotFoundException;
import guru.springframework.repositories.RecipeRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

@Service
@Slf4j
@AllArgsConstructor
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final RecipeCommandToRecipe recipeCommandToRecipe;
    private final RecipeToRecipeCommand recipeToRecipeCommand;

    @Override
    public Set<Recipe> findAllRecipes() {
        log.debug("Entering findAllRecipes");
        Set<Recipe> recipes = new TreeSet<>();
        recipeRepository.findAll().iterator().forEachRemaining(recipes::add);
        log.info("Exiting findAllRecipes");
        return recipes;
    }

    @Override
    public Recipe findById(long id) {
        log.info("Entering findById for: " + id );
        Optional<Recipe> optionalRecipe = recipeRepository.findById(id);
        if(optionalRecipe.isEmpty()) {
            throw new NotFoundException("Recipe is not present for ID: " + String.valueOf(id));
        }
        Recipe recipe = optionalRecipe.orElseGet(() -> null);
        log.info("Exiting findById for: " + id );
        return recipe;
    }

    @Override
    @Transactional
    public RecipeCommand findCommandById(long id) {
        log.info("Entering findCommandById for: " + id);
        Recipe recipe = findById(id);
        RecipeCommand recipeCommand = recipeToRecipeCommand.convert( recipe );
        log.info("Exiting findCommandById for: " + id );
        return recipeCommand;
    }

    @Override
    @Transactional
    public RecipeCommand saveRecipeCommand(RecipeCommand recipeCommand) {
        log.info("Entering saveRecipeCommand for:" + recipeCommand.getDescription());
        Recipe recipe = recipeCommandToRecipe.convert(recipeCommand);
        Recipe savedRecipe = recipeRepository.save(recipe);
        RecipeCommand detachRecipeCommand = recipeToRecipeCommand.convert(savedRecipe);
        log.info("Exiting savRecipeCommand for: " + recipeCommand.getDescription());
        return detachRecipeCommand;
    }

    @Override
    public void deleteById(long id) {
        log.info("Entering deleteById for: " + id );
        recipeRepository.deleteById(id);
        log.info("Entering deleteById for: " + id );
    }


}
