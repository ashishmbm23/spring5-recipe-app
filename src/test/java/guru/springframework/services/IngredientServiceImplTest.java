package guru.springframework.services;

import guru.springframework.command.IngredientCommand;
import guru.springframework.converters.IngredientToIngredientCommand;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IngredientServiceImplTest {

    public static final long ID = 1L;
    public static final long INGREDIENT_ID = 2L;
    @Mock
    RecipeService recipeService;
    @Mock
    IngredientToIngredientCommand ingredientToIngredientCommand;

    @InjectMocks
    IngredientServiceImpl ingredientService;

    @Test
    void findIngredientCommandByRecipeIdAndId() {
        Recipe recipe = new Recipe();
        recipe.setId(ID);

        Ingredient ingredient = new Ingredient();
        ingredient.setId(INGREDIENT_ID);

        recipe.addIngredient( ingredient );

        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setId(INGREDIENT_ID);

        when( recipeService.findById(anyLong())).thenReturn(recipe);
        when( ingredientToIngredientCommand.convert(any())).thenReturn(ingredientCommand);

        IngredientCommand resultIngredientCommand = ingredientService.findIngredientCommandByRecipeIdAndId
                ( ID, INGREDIENT_ID );

        assertNotNull( resultIngredientCommand );
        assertEquals( INGREDIENT_ID, resultIngredientCommand.getId() );
        verify( recipeService, times(1) ).findById(anyLong());
        verify( ingredientToIngredientCommand, times(1)).convert( any() );
    }
}