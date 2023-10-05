package guru.springframework.services;

import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.stream.StreamSupport;

/**
 * @Author: harshitasain
 * @Date: 02/10/23
 **/
@Service
@Slf4j
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService{

    private final RecipeRepository recipeRepository;

    @Override
    public void saveImage(Long recipeId, MultipartFile image) {
        log.info( this.getClass().getName() + ": Received image file for recipeId: " + recipeId );
        try{
            Recipe recipe = recipeRepository.findById( recipeId ).get();
            if( recipe != null ){
                byte[] sourceImageBytes = image.getBytes();
                Byte[] imageBytes = new Byte[sourceImageBytes.length];
                Arrays.setAll( imageBytes, n -> sourceImageBytes[n]);
                recipe.setImage( imageBytes );
                recipeRepository.save( recipe );
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        log.info( this.getClass().getName() + ": Exiting saveImage for recipeId: " + recipeId);
    }
}
