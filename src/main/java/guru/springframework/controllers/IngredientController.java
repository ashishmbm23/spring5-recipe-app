package guru.springframework.controllers;

import guru.springframework.command.IngredientCommand;
import guru.springframework.command.RecipeCommand;
import guru.springframework.command.UnitOfMeasureCommand;
import guru.springframework.services.IngredientService;
import guru.springframework.services.RecipeService;
import guru.springframework.services.UnitOfMeasureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * @Author: harshitasain
 * @Date: 30/09/23
 **/

@Controller
@Slf4j
@RequiredArgsConstructor
public class IngredientController {

    private final RecipeService recipeService;
    private final IngredientService ingredientService;
    private final UnitOfMeasureService unitOfMeasureService;

    @GetMapping("/recipe/{recipeId}/ingredients")
    public String listIngredients(@PathVariable String recipeId, Model model){
        log.info(this.getClass().getName() + ":Entering listIngredients");
        RecipeCommand recipe = recipeService.findCommandById(Long.parseLong(recipeId));
        model.addAttribute("recipe", recipe);
        log.info(this.getClass().getName() + ":Exiting listIngredients");
        return "recipe/ingredient/list";
    }

    @GetMapping("/recipe/{recipeId}/ingredients/{ingredientId}/show")
    public String findIngredientById(@PathVariable String recipeId, @PathVariable String ingredientId,
                                     Model model) {
        log.info(this.getClass().getName() + ":Entering findIngredientById");
        IngredientCommand ingredientCommand = ingredientService.
                findIngredientCommandByRecipeIdAndId( Long.parseLong(recipeId), Long.parseLong(ingredientId) );
        model.addAttribute("ingredient", ingredientCommand);
        log.info(this.getClass().getName() +":Exiting findIngredientById");
        return "recipe/ingredient/show";
    }

    @GetMapping("/recipe/{recipeId}/ingredients/{ingredientId}/update")
    public String updateRecipe(@PathVariable String recipeId, @PathVariable String ingredientId,
                               Model model){
        log.info( this.getClass().getName() + ": Entering updateRecipe" );
        IngredientCommand ingredientCommand = ingredientService.findIngredientCommandByRecipeIdAndId(
                Long.parseLong( recipeId ), Long.parseLong(ingredientId));
        Set<UnitOfMeasureCommand> unitOfMeasureCommandSet = unitOfMeasureService.listAllUOMs();
        model.addAttribute("ingredient", ingredientCommand );
        model.addAttribute( "uoms", unitOfMeasureCommandSet );
        return "recipe/ingredient/ingredientform";
    }

    @RequestMapping("/recipe/{recipeId}/ingredients/save")
    @PostMapping
    public String saveOrUpdate(@ModelAttribute IngredientCommand ingredientCommand, @PathVariable String recipeId,
                                 Model model){
        log.info(this.getClass().getName() +":Entering saveOrUpdate");
        IngredientCommand savedIngredientCommand = ingredientService.saveIngredientCommand(ingredientCommand);
        log.info(this.getClass().getName() +":Exiting saveOrUpdate");
        return "redirect:/recipe/" + recipeId + "/ingredients/" + savedIngredientCommand.getId() + "/show";
    }

    @PostMapping("/recipe/{recipeId}/ingredients/new")
    public String newIngredient(@PathVariable String recipeId, Model model) {
        log.info( this.getClass().getName() + ": Entering newIngredient");
        RecipeCommand recipeCommand = recipeService.findCommandById( Long.parseLong( recipeId ));

        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setRecipeId( Long.parseLong( recipeId) );
        ingredientCommand.setUnitOfMeasure( new UnitOfMeasureCommand());
        model.addAttribute("ingredient", ingredientCommand );

        Set<UnitOfMeasureCommand> unitOfMeasureCommandSet = unitOfMeasureService.listAllUOMs();
        model.addAttribute("uoms", unitOfMeasureCommandSet);

        log.info( this.getClass().getName() + ": Exiting newIngredient");
        return "recipe/ingredient/ingredientform";
    }

    @GetMapping("/recipe/{recipeId}/ingredients/{ingredientId}/delete")
    public String deleteIngredientById(@PathVariable String recipeId, @PathVariable String ingredientId,
                                       Model model){
        log.info( this.getClass().getName() + ": Entering deleteIngredientById" );
        ingredientService.deleteById( Long.parseLong( recipeId), Long.parseLong( ingredientId ));
        log.info( this.getClass().getName() + ": Exiting deleteIngredientById" );
        return "redirect:/recipe/" + recipeId + "/ingredients" ;
    }
}
