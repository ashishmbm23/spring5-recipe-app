package guru.springframework.controllers;

import guru.springframework.command.IngredientCommand;
import guru.springframework.command.RecipeCommand;
import guru.springframework.command.UnitOfMeasureCommand;
import guru.springframework.services.IngredientService;
import guru.springframework.services.RecipeService;
import guru.springframework.services.UnitOfMeasureService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class IngredientControllerTest {

    public static final long RECIPE_ID = 1L;
    public static final long INGREDIENT_ID = 2L;
    public static final long UOM_ID_1 = 11L;
    public static final long UOM_ID_2 = 12L;
    @Mock
    RecipeService recipeService;
    @Mock
    IngredientService ingredientService;
    @Mock
    UnitOfMeasureService unitOfMeasureService;
    @InjectMocks
    IngredientController ingredientController;

    MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(ingredientController).build();
    }

    private static RecipeCommand getRecipeCommand() {
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(RECIPE_ID);
        return recipeCommand;
    }

    private static IngredientCommand getIngredientCommand() {
        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setId(INGREDIENT_ID);
        ingredientCommand.setRecipeId(RECIPE_ID);
        return ingredientCommand;
    }

    private static Set<UnitOfMeasureCommand> getUnitOfMeasureCommands() {
        Set<UnitOfMeasureCommand> unitOfMeasures = new HashSet<>();
        UnitOfMeasureCommand unitOfMeasure1 = new UnitOfMeasureCommand();
        unitOfMeasure1.setId(UOM_ID_1);
        UnitOfMeasureCommand unitOfMeasure2 = new UnitOfMeasureCommand();
        unitOfMeasure2.setId(UOM_ID_2);
        unitOfMeasures.add(unitOfMeasure1);
        unitOfMeasures.add(unitOfMeasure2);
        return unitOfMeasures;
    }

    private void mockRecipeServiceFindCommandById() {
        RecipeCommand recipeCommand = getRecipeCommand();
        when(recipeService.findCommandById(anyLong())).thenReturn(recipeCommand);
    }

    private void mockUnitOfMeasureServiceListUOMs() {
        Set<UnitOfMeasureCommand> unitOfMeasures = getUnitOfMeasureCommands();
        when(unitOfMeasureService.listAllUOMs()).thenReturn(unitOfMeasures);
    }

    private void mockIngredientServiceFindIngredientCommandByRecipeIdAndId() {
        IngredientCommand ingredientCommand = getIngredientCommand();
        when(ingredientService.findIngredientCommandByRecipeIdAndId(anyLong(), anyLong())).thenReturn(ingredientCommand);
    }

    private void mockIngredientServiceSaveIngredientCommand() {
        IngredientCommand ingredientCommand = getIngredientCommand();
        when(ingredientService.saveIngredientCommand(any())).thenReturn(ingredientCommand);
    }

    @Test
    void listIngredients() throws Exception {
        mockRecipeServiceFindCommandById();

        mockMvc.perform(get("/recipe/" + RECIPE_ID + "/ingredients")).andExpect(status().isOk()).andExpect(model().attributeExists("recipe")).andExpect(view().name("recipe/ingredient/list"));

        verify(recipeService, times(1)).findCommandById(anyLong());
    }


    @Test
    void findIngredientById() throws Exception {
        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setId(INGREDIENT_ID);

        when(ingredientService.findIngredientCommandByRecipeIdAndId(anyLong(), anyLong())).thenReturn(ingredientCommand);

        mockMvc.perform(get("/recipe/" + RECIPE_ID + "/ingredients/" + INGREDIENT_ID + "/show")).andExpect(status().isOk()).andExpect(view().name("recipe/ingredient/show")).andExpect(model().attributeExists("ingredient"));

        verify(ingredientService, times(1)).findIngredientCommandByRecipeIdAndId(anyLong(), anyLong());
    }

    @Test
    void updateRecipe() throws Exception {
        mockIngredientServiceFindIngredientCommandByRecipeIdAndId();
        mockUnitOfMeasureServiceListUOMs();

        mockMvc.perform(get("/recipe/" + RECIPE_ID + "/ingredients/" + INGREDIENT_ID + "/update")).andExpect(status().isOk()).andExpect(model().attributeExists("ingredient")).andExpect(model().attributeExists("uoms")).andExpect(view().name("recipe/ingredient/ingredientform"));

        verify(ingredientService, times(1)).findIngredientCommandByRecipeIdAndId(anyLong(), anyLong());
        verify(unitOfMeasureService, times(1)).listAllUOMs();
    }

    @Test
    void saveOrUpdate() throws Exception {
        mockIngredientServiceSaveIngredientCommand();

        mockMvc.perform(post("/recipe/" + RECIPE_ID + "/ingredients/save").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("id", String.valueOf(RECIPE_ID)).param("description", "")).andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/recipe/" + RECIPE_ID + "/ingredients/" + INGREDIENT_ID + "/show"));

        verify(ingredientService, times(1)).saveIngredientCommand(any());
    }


    @Test
    void newIngredient() throws Exception {
        mockRecipeServiceFindCommandById();
        mockUnitOfMeasureServiceListUOMs();

        mockMvc.perform( post("/recipe/" + RECIPE_ID + "/ingredients/new"))
                .andExpect( status().isOk() )
                .andExpect( view().name("recipe/ingredient/ingredientform"))
                .andExpect( model().attributeExists("ingredient"))
                .andExpect( model().attributeExists( "uoms" ));

        verify( recipeService, times(1)).findCommandById( anyLong() );
        verify( unitOfMeasureService, times(1)).listAllUOMs();
    }

    @Test
    void deleteIngredientById() throws Exception{
        doNothing().when( ingredientService ).deleteById( anyLong(), anyLong() );

        mockMvc.perform( get( "/recipe/" + RECIPE_ID + "/ingredients/" + INGREDIENT_ID + "/delete"))
                        .andExpect( status().is3xxRedirection() )
                        .andExpect( view().name( "redirect:/recipe/" + RECIPE_ID + "/ingredients"));

        verify( ingredientService, times( 1)).deleteById( anyLong(), anyLong());
    }
}