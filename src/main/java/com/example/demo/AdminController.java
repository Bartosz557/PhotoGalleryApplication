package com.example.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AdminController {

    // request to change the view
    @PostMapping("/create-gallery")
    public ResponseEntity<String> createGallery() {
        return ResponseEntity.ok().build();
    }

    // changing the view
    @RequestMapping("/go-to-creating-the-gallery")
    public RedirectView localRedirect() {
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("/creating-gallery");
        return redirectView;
    }

    //upload gallery
    @RequestMapping("/upload")
    public ResponseEntity<String> uploadGallery(
            @RequestParam("galleryName") String galleryName,
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("photos") MultipartFile[] photos) {
        List<String> filenames = new ArrayList<>();
        for (MultipartFile photo : photos) {
            String filename = photo.getOriginalFilename();
            try {
                Files.write(Paths.get("src/main/resources/static/images/" + filename), photo.getBytes()); // saving images
                    filenames.add(filename);
            }catch (IOException e) {
                    e.printStackTrace();
                }
        }
        SaveGalleryToDatabase saveGallery = new SaveGalleryToDatabase(galleryName,username,password,filenames);
        saveGallery.saveToDatabase();
        return ResponseEntity.ok().build();
    }

}
