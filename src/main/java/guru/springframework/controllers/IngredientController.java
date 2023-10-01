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

    @RequestMapping("/recipe/{recipeId}/ingredients")
    @GetMapping
    public String listIngredients(@PathVariable String recipeId, Model model){
        log.info(this.getClass().getName() + ":Entering listIngredients");
        RecipeCommand recipe = recipeService.findCommandById(Long.parseLong(recipeId));
        model.addAttribute("recipe", recipe);
        log.info(this.getClass().getName() + ":Exiting listIngredients");
        return "recipe/ingredient/list";
    }

    @RequestMapping("/recipe/{recipeId}/ingredients/{ingredientId}/show")
    @GetMapping
    public String findIngredientById(@PathVariable String recipeId, @PathVariable String ingredientId,
                                     Model model) {
        log.info(this.getClass().getName() + ":Entering findIngredientById");
        IngredientCommand ingredientCommand = ingredientService.
                findIngredientCommandByRecipeIdAndId( Long.parseLong(recipeId), Long.parseLong(ingredientId) );
        model.addAttribute("ingredient", ingredientCommand);
        log.info(this.getClass().getName() +":Exiting findIngredientById");
        return "recipe/ingredient/show";
    }

    @RequestMapping("/recipe/{recipeId}/ingredients/{ingredientId}/update")
    @GetMapping
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
}
