package guru.springframework.controllers;

import guru.springframework.command.RecipeCommand;
import guru.springframework.domain.Recipe;
import guru.springframework.exception.NotFoundException;
import guru.springframework.services.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
class RecipeControllerTest {

    public static final long ID = 1L;
    public static final long RECIPE_CMD_ID = 1L;
    public static final String RECIPE_FORM = "/recipe/recipeform";
    @Mock
    RecipeService recipeService;

    @InjectMocks
    RecipeController recipeController;

    MockMvc mockMvc;

    @BeforeEach
    public void setup(){
        mockMvc = MockMvcBuilders.standaloneSetup(recipeController)
                                 .setControllerAdvice(new ControllerExceptionAdvice())
                                 .build();
    }

    @Test
    void testGetRecipeById() throws Exception{
        Recipe recipe = new Recipe();
        recipe.setId(ID);
        when(recipeService.findById(anyLong())).thenReturn(recipe);

        mockMvc.perform(get("/recipe/1/show"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/show"))
                .andExpect(model().attributeExists("recipe"));

    }

    @Test
    void testNewRecipe() throws Exception {

        mockMvc.perform( get("/recipe/new" ))
                .andExpect( status().isOk() )
                .andExpect( model().attributeExists("recipe" ))
                .andExpect( view().name("recipe/recipeform"));

    }

    @Test
    void testSaveOrUpdateRecipe() throws Exception {

        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(RECIPE_CMD_ID);
        when( recipeService.saveRecipeCommand(any())).thenReturn(recipeCommand);
        mockMvc.perform( post("/recipe/save" )
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", "")
                        .param( "description", "this is a description")
                        .param( "prepTime", "10")
                        .param( "cookTime", "10")
                        .param( "url", "http:\\www.google.com")
                        .param( "directions", "see from google")
                )
                .andExpect( status().is3xxRedirection() )
                .andExpect( view().name("redirect:/recipe/" + RECIPE_CMD_ID + "/show" ));
    }

    @Test
    void testUpdateRecipe() throws Exception{
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(RECIPE_CMD_ID);
        when( recipeService.findCommandById(anyLong())).thenReturn(recipeCommand);
        mockMvc.perform( get("/recipe/1/update" ))
                .andExpect( status().isOk() )
                .andExpect( model().attributeExists("recipe" ))
                .andExpect( view().name(RECIPE_FORM));
    }

    @Test
    void testDeleteRecipe() throws Exception{
        mockMvc.perform( get("/recipe/1/delete" ))
                .andExpect( status().is3xxRedirection() )
                .andExpect( view().name("redirect:/" ));

        verify(recipeService, times(1)).deleteById(anyLong());
    }

    @Test
    void testGetRecipeNotFound() throws Exception{
        Recipe recipe = new Recipe();
        recipe.setId(ID);
        when(recipeService.findById(anyLong())).thenThrow(NotFoundException.class);

        mockMvc.perform( get("/recipe/1/show"))
                .andExpect( status().isNotFound() );
    }

    @Test
    void handleNotFound() throws Exception{
        when( recipeService.findById(anyLong())).thenThrow(NotFoundException.class);
        mockMvc.perform( get("/recipe/4/show"))
                .andExpect( view().name("404"))
                .andExpect( status().isNotFound());
    }

    @Test
    void handleBadRequest() throws Exception {
        mockMvc.perform( get("/recipe/asdf/show"))
                .andExpect( view().name("400"))
                .andExpect( status().isBadRequest());
    }

    @Test
    public void saveOrUpdateNotValidCase() throws Exception{

        mockMvc.perform( post("/recipe/save")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", "")
                        .param("description", "this is a description"))
                .andExpect( view().name("recipe/recipeform"))
                .andExpect( status().isOk() );
    }
}