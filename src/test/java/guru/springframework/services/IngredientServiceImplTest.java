package guru.springframework.services;

import guru.springframework.command.IngredientCommand;
import guru.springframework.command.UnitOfMeasureCommand;
import guru.springframework.converters.IngredientCommandToIngredient;
import guru.springframework.converters.IngredientToIngredientCommand;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import guru.springframework.domain.UnitOfMeasure;
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.repositories.UnitOfMeasureRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IngredientServiceImplTest {

    public static final long ID = 1L;
    public static final long INGREDIENT_ID = 2L;
    public static final long UOM_ID = 3L;
    @Mock
    RecipeRepository recipeRepository;
    @Mock
    UnitOfMeasureRepository unitOfMeasureRepository;
    @Mock
    IngredientToIngredientCommand ingredientToIngredientCommand;
    @Mock
    IngredientCommandToIngredient ingredientCommandToIngredient;
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

        when( recipeRepository.findById(anyLong())).thenReturn(Optional.of(recipe));
        when( ingredientToIngredientCommand.convert(any())).thenReturn(ingredientCommand);

        IngredientCommand resultIngredientCommand = ingredientService.findIngredientCommandByRecipeIdAndId
                ( ID, INGREDIENT_ID );

        assertNotNull( resultIngredientCommand );
        assertEquals( INGREDIENT_ID, resultIngredientCommand.getId() );
        verify( recipeRepository, times(1) ).findById(anyLong());
        verify( ingredientToIngredientCommand, times(1)).convert( any() );
    }

    @Test
    void saveIngredientCommand() {
        Recipe recipe = getRecipe();
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setId( UOM_ID );

        when( recipeRepository.findById( anyLong())).thenReturn(Optional.of(recipe));
        when( unitOfMeasureRepository.findById( anyLong())).thenReturn(Optional.of(unitOfMeasure));
        when( recipeRepository.save( any())).thenReturn( recipe );

        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setId(INGREDIENT_ID);
        ingredientCommand.setRecipeId( ID );
        UnitOfMeasureCommand unitOfMeasureCommand = new UnitOfMeasureCommand();
        unitOfMeasureCommand.setId( UOM_ID );
        ingredientCommand.setUnitOfMeasure(unitOfMeasureCommand);
        when( ingredientToIngredientCommand.convert( any() )).thenReturn( ingredientCommand );

        IngredientCommand result = ingredientService.saveIngredientCommand( ingredientCommand );

        assertNotNull( result );
        assertEquals( INGREDIENT_ID, result.getId() );
        assertEquals( ID, result.getRecipeId() );
        verify( recipeRepository, times(1)).findById(anyLong());
        verify( unitOfMeasureRepository, times(1)).findById( anyLong() );
        verify( recipeRepository, times(1)).save(any());
        verify( ingredientToIngredientCommand, times(1)).convert( any() );

    }

    private Recipe getRecipe() {
        Recipe recipe = new Recipe();
        recipe.setId( ID );
        Ingredient ingredient = new Ingredient();
        ingredient.setId( INGREDIENT_ID );
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setId(UOM_ID);
        ingredient.setUnitOfMeasure(unitOfMeasure);
        Set<Ingredient> ingredientSet = new HashSet<>();
        ingredientSet.add( ingredient );
        recipe.setIngredients( ingredientSet );
        return recipe;
    }

    @Test
    void deleteById() {
        Recipe recipe = getRecipe();

        when( recipeRepository.findById( anyLong() )).thenReturn( Optional.of( recipe) );
        when( recipeRepository.save( any() )).thenReturn( recipe );

        ingredientService.deleteById(ID, INGREDIENT_ID);

        verify( recipeRepository, times(1)).findById( anyLong() );
        verify( recipeRepository, times( 1)).save( any() );
    }
}