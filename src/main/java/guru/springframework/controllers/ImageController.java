package guru.springframework.controllers;

import guru.springframework.services.ImageService;
import guru.springframework.services.RecipeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: harshitasain
 * @Date: 02/10/23
 **/
@Controller
@RequiredArgsConstructor
@Slf4j
public class ImageController {

    private final RecipeService recipeService;
    private final ImageService imageService;

    @GetMapping("/recipe/{recipeId}/image")
    public String changeImage(@PathVariable String recipeId, Model model){
        log.info( this.getClass().getName() + ": Entering changeImage for recipeId: " + recipeId );
        model.addAttribute( "recipe", recipeService.findCommandById( Long.parseLong( recipeId)));
        log.info( this.getClass().getName() + ": Exiting changeImage for recipeId: " + recipeId );
        return "recipe/imageuploadform";
    }

    @PostMapping("/recipe/{recipeId}/image")
    public String uploadImage(@PathVariable String recipeId, @RequestParam("imageUploadId") MultipartFile file){
        log.info( this.getClass().getName() + ": Entering upload Image" );
        imageService.saveImage( Long.parseLong( recipeId), file );
        log.info( this.getClass().getName() + ": Entering upload Image" );
        return "redirect:/recipe/" + recipeId + "/show";
    }
}
