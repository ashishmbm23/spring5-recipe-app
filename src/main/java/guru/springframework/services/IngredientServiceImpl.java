package guru.springframework.services;

import guru.springframework.command.IngredientCommand;
import guru.springframework.converters.IngredientCommandToIngredient;
import guru.springframework.converters.IngredientToIngredientCommand;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.repositories.UnitOfMeasureRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @Author: harshitasain
 * @Date: 30/09/23
 **/
@Service
@RequiredArgsConstructor
@Slf4j
public class IngredientServiceImpl implements IngredientService{

    final RecipeRepository recipeRepository;
    final UnitOfMeasureRepository unitOfMeasureRepository;
    final IngredientToIngredientCommand ingredientToIngredientCommand;
    final IngredientCommandToIngredient ingredientCommandToIngredient;

    @Override
    public IngredientCommand findIngredientCommandByRecipeIdAndId(Long recipeId, Long id) {
        log.info("Entering findIngredientCommandByRecipeIdAndId for recipeId: " + recipeId + " and IngredientId: "
                + id );
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);
        if( !recipeOptional.isPresent() ){
            log.error("Recipe is not present for id: " + recipeId );
            return null;
        }
        Recipe recipe = recipeOptional.get();
        Optional<IngredientCommand> resultIngredientCommand
                = recipe.getIngredients().stream()
                    .filter( (ingredient -> ingredient.getId().equals(id)) )
                    .map(ingredientToIngredientCommand::convert)
                    .findFirst();
        log.info(" Exiting findIngredientCommandByRecipeIdAndId for recipeId: " + recipeId + " and IngredientId: "
                + id );
        return resultIngredientCommand.orElse(null);
    }

    @Override
    @Transactional
    public IngredientCommand saveIngredientCommand(IngredientCommand ingredientCommand) {
        log.info("Entering saveIngredientCommnad for recipe Id : " + ingredientCommand.getRecipeId() +
                " and ingredient Id : " + ingredientCommand.getId() );

        Optional<Recipe> recipeOptionl = recipeRepository.findById( ingredientCommand.getRecipeId() );

        if(!recipeOptionl.isPresent()){
            log.error("Recipe is not present");
            return new IngredientCommand();
        }
        else
        {
            Recipe recipe = recipeOptionl.get();
            Optional<Ingredient> ingredientOptional = recipe.getIngredients()
                                            .stream()
                                            .filter( (ingredient) -> ingredient.getId().equals(ingredientCommand.getId()))
                                            .findFirst();
            if( ingredientOptional.isPresent() ){
                Ingredient ingredient = ingredientOptional.get();
                ingredient.setAmount( ingredientCommand.getAmount() );
                ingredient.setDescription( ingredientCommand.getDescription() );
                ingredient.setUnitOfMeasure( unitOfMeasureRepository.findById(
                        ingredientCommand.getUnitOfMeasure().getId() )
                        .orElseThrow(() -> new RuntimeException("UOM_NOT_FOUND" +
                                ingredientCommand.getUnitOfMeasure().getId())));
            }else{
                Ingredient ingredient = ingredientCommandToIngredient.convert( ingredientCommand );
                ingredient.setRecipe(recipe);
                recipe.addIngredient( ingredient );
            }
            Recipe savedRecipe = recipeRepository.save( recipe );

            Optional<Ingredient> savedIngredientCommandOptional = recipe.getIngredients().stream()
                    .filter( (ingredient) -> ingredient.getId().equals( ingredientCommand.getId() ))
                    .findFirst();

            if( !savedIngredientCommandOptional.isPresent() ){
                savedIngredientCommandOptional = recipe.getIngredients().stream()
                        .filter( (ingredient) -> ingredient.getDescription().equals( ingredientCommand.getDescription()))
                        .filter( (ingredient) -> ingredient.getAmount().equals( ingredientCommand.getAmount() ))
                        .filter( (ingredient) -> ingredient.getUnitOfMeasure().getId().equals
                                ( ingredientCommand.getUnitOfMeasure().getId() ))
                        .findFirst();
            }

            IngredientCommand result = ingredientToIngredientCommand.convert( savedIngredientCommandOptional.get() );
            log.info( "Exiting saveIngredientCommand for recipe Id :" + ingredientCommand.getRecipeId() +
                   " and ingredient Id : " + ingredientCommand.getId() );
            return result;
        }
    }

    @Override
    public void deleteById(Long recipeId, Long ingredientId) {
        log.info( this.getClass().getName() +": Entering deleteById for recipeId: " + recipeId + " and ingredientId: " +
                ingredientId );
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);
        if( !recipeOptional.isPresent() ){
            log.error("Recipe for :" + recipeId + " is not present." );
            return;
        }
        Recipe recipe = recipeOptional.get();
        Optional<Ingredient> ingredientOptional =
                recipe.getIngredients().stream()
                                        .filter(  (ingredient) -> ingredient.getId().equals( ingredientId ) )
                                        .findFirst() ;
        if( ingredientOptional.isPresent() ){
            Ingredient ingredientToDelete = ingredientOptional.get();
            ingredientToDelete.setRecipe( null );
            recipe.getIngredients().remove( ingredientToDelete );
            recipeRepository.save(recipe);
        }

        log.info( this.getClass().getName() + ": exiting deleteById" );
    }
}
