package guru.springframework.controllers;

import guru.springframework.command.IngredientCommand;
import guru.springframework.command.RecipeCommand;
import guru.springframework.services.IngredientService;
import guru.springframework.services.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@ExtendWith(MockitoExtension.class)
class IngredientControllerTest {

    public static final long RECIPE_ID = 1L;
    public static final long INGREDIENT_ID = 2L;
    @Mock
    RecipeService recipeService;
    @Mock
    IngredientService ingredientService;
    @InjectMocks
    IngredientController ingredientController;

    MockMvc mockMvc;
    @BeforeEach
    public void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup(ingredientController).build();
    }

    @Test
    void listIngredients() throws Exception {
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(RECIPE_ID);
        when( recipeService.findCommandById(anyLong())).thenReturn(recipeCommand);

        mockMvc.perform( get("/recipe/" + RECIPE_ID + "/ingredients" ))
                .andExpect( status().isOk() )
                .andExpect( model().attributeExists("recipe"))
                .andExpect( view().name("recipe/ingredient/list" ));

        verify(recipeService, times(1)).findCommandById(anyLong());
    }

    @Test
    void findIngredientById() throws Exception{
        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setId(INGREDIENT_ID);

        when( ingredientService.findIngredientCommandByRecipeIdAndId(anyLong(), anyLong() ))
                .thenReturn( ingredientCommand );


        mockMvc.perform( get("/recipe/" + RECIPE_ID + "/ingredients/" + INGREDIENT_ID + "/show" ))
                .andExpect( status().isOk() )
                .andExpect( view().name("recipe/ingredient/show" ))
                .andExpect( model().attributeExists("ingredient" ));

        verify( ingredientService, times(1)).findIngredientCommandByRecipeIdAndId(anyLong(), anyLong());

    }
}