package guru.springframework.services;

import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ImageServiceImplTest {

    public static final long RECIPE_ID = 1L;
    @Mock
    private RecipeRepository recipeRepository;

    @InjectMocks
    private ImageServiceImpl imageService;

    @Test
    void saveImage() throws Exception {

        MultipartFile multipartFile = new MockMultipartFile("imageUploadId", "testing.txt", "text/plain"
        , "Ashish".getBytes());

        Recipe recipe = new Recipe();
        recipe.setId(RECIPE_ID);

        when( recipeRepository.findById( anyLong() )).thenReturn( Optional.of(recipe));

        imageService.saveImage(RECIPE_ID, multipartFile);
        ArgumentCaptor<Recipe> recipeArgumentCaptor = ArgumentCaptor.forClass(Recipe.class);

        verify( recipeRepository, times(1)).save( recipeArgumentCaptor.capture());
        Recipe savedRecipe =  recipeArgumentCaptor.getValue();

        assertNotNull( savedRecipe );
        assertEquals(multipartFile.getBytes().length, savedRecipe.getImage().length);
    }
}