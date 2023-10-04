package guru.springframework.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: harshitasain
 * @Date: 02/10/23
 **/
@Service
@Slf4j
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService{

    @Override
    public void saveImage(Long recipeId, MultipartFile image) {
        log.info( this.getClass().getName() + ": Received image file for recipeId: " + recipeId );
    }
}
