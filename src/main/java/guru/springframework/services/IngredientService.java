package guru.springframework.services;

import guru.springframework.command.IngredientCommand;

/**
 * @Author: harshitasain
 * @Date: 30/09/23
 **/
public interface IngredientService {
    IngredientCommand findIngredientCommandByRecipeIdAndId(Long recipeId, Long id);

    IngredientCommand saveIngredientCommand(IngredientCommand ingredientCommand);

    void deleteById(Long recipeId, Long ingredientId);
}
