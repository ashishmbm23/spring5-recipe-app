package guru.springframework.controllers;

import guru.springframework.command.RecipeCommand;
import guru.springframework.services.ImageService;
import guru.springframework.services.RecipeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ImageControllerTest {

    public static final long RECIPE_ID = 1L;
    @Mock
    private RecipeService recipeService;
    @Mock
    private ImageService imageService;
    @InjectMocks
    private ImageController imageController;

    MockMvc mockMvc;

    @BeforeEach
    public void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup( imageController )
                .setControllerAdvice(new ControllerExceptionAdvice())
                .build();
    }

    private RecipeCommand getRecipeCommand(){
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(RECIPE_ID);
        return recipeCommand;
    }

    private void mockRecipeServiceFindCommandById(){
        when( recipeService.findCommandById(anyLong())).thenReturn( getRecipeCommand() );
    }

    private void mockImageServiceSaveImage(){
        doNothing().when( imageService ).saveImage( anyLong(), any() );
    }
    @Test
    void changeImage() throws Exception{
        mockRecipeServiceFindCommandById();

        mockMvc.perform( get("/recipe/" + RECIPE_ID + "/image" ))
                .andExpect( status().isOk())
                .andExpect( model().attributeExists( "recipe" ))
                .andExpect( view().name("recipe/imageuploadform" ));

        verify( recipeService, times(1)).findCommandById( anyLong());
    }

    @Test
    void uploadImage() throws Exception{
        mockImageServiceSaveImage();
        MockMultipartFile multipartFile =
                new MockMultipartFile("imageUploadId", "testing.txt", "text/plain",
                        "Ashish".getBytes());
        mockMvc.perform( multipart("/recipe/" + RECIPE_ID + "/image" ).file(multipartFile))
                .andExpect( status().is3xxRedirection())
                .andExpect( view().name("redirect:/recipe/" + RECIPE_ID + "/show"));

        verify( imageService, times(1)).saveImage( anyLong(), any());
    }

    @Test
    void renderImageFromDB() throws Exception {

        String text = "Ashish";
        byte[] sourceBytes = text.getBytes();
        Byte[] targetBytes = new Byte[sourceBytes.length];
        Arrays.setAll(targetBytes, n -> sourceBytes[n++]);

        RecipeCommand recipeCommand = getRecipeCommand();
        recipeCommand.setImage(targetBytes);

        when(recipeService.findCommandById(anyLong())).thenReturn(recipeCommand);

        MockHttpServletResponse response = mockMvc.perform( get("/recipe/" + RECIPE_ID + "/recipeImage"))
                .andExpect( status().isOk() )
                .andReturn().getResponse();

        byte [] responseBytes = response.getContentAsByteArray();
        Assertions.assertEquals( sourceBytes.length, responseBytes.length);
    }

    @Test
    public void testImageNumberFormatException() throws Exception{

        mockMvc.perform( get("/recipe/asdj/recipeImage"))
                .andExpect( view().name("400"))
                .andExpect( status().isBadRequest());

    }
}