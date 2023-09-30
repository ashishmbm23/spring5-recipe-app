package guru.springframework.services;

import guru.springframework.command.IngredientCommand;
import guru.springframework.converters.IngredientToIngredientCommand;
import guru.springframework.domain.Recipe;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @Author: harshitasain
 * @Date: 30/09/23
 **/
@Service
@RequiredArgsConstructor
public class IngredientServiceImpl implements IngredientService{

    final RecipeService recipeService;
    final IngredientToIngredientCommand ingredientToIngredientCommand;

    @Override
    public IngredientCommand findIngredientCommandByRecipeIdAndId(Long recipeId, Long id) {
        Recipe recipe = recipeService.findById(recipeId);
        Optional<IngredientCommand> resultIngredientCommand
                = recipe.getIngredients().stream()
                    .filter( (ingredient -> ingredient.getId().equals(id)) )
                    .map(ingredientToIngredientCommand::convert)
                    .findFirst();
        return resultIngredientCommand.orElse(null);
    }
}
