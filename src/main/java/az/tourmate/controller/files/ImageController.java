package az.tourmate.controller.files;

import az.tourmate.services.files.ImageService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/files")
public class ImageController {
    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/{branchId}")
    @PreAuthorize("hasAnyAuthority('owner:create','manager:create')")
    public ResponseEntity<String> uploadImageToBranch
            (@RequestParam("file") MultipartFile file, Principal connectedUser,@PathVariable("branchId") Long branchId){
        return ResponseEntity.ok(imageService.uploadFileToBranches(file,connectedUser,branchId));
    }

    @GetMapping("/{branchId}/getImages")
    public void getImages(@PathVariable("branchId") Long branchId, HttpServletResponse response){
        imageService.getFiles(branchId,response);
    }


}
