package guru.springframework.services;

import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: harshitasain
 * @Date: 02/10/23
 **/
public interface ImageService {
    void saveImage(Long recipeId, MultipartFile image);
}
