package az.tourmate.controller.users;

import az.tourmate.dtos.user.ChangePasswordRequest;
import az.tourmate.dtos.user.UserProfileResponse;
import az.tourmate.services.files.ImageService;
import az.tourmate.services.management.ManagementService;
import az.tourmate.services.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;
    private final ImageService imageService;
    private final ManagementService managementService;

    public UserController(UserService userService, ImageService imageService, ManagementService managementService) {
        this.userService = userService;
        this.imageService = imageService;
        this.managementService = managementService;
    }


    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword
            (@RequestBody ChangePasswordRequest request,Principal connectedUser){
        userService.changePasswordOfUser(request,connectedUser);
        return ResponseEntity.ok("Your password changed")   ;
    }

    @PostMapping("/{managementId}/add-user")
    @PreAuthorize("hasAuthority('owner:create')")
    public ResponseEntity<Boolean> addUserToManagement(@PathVariable("managementId") Long managementId,
                                                       Principal connectedUser,@RequestBody String email){
        return ResponseEntity.ok(managementService.addAdminToManagement(connectedUser,email,managementId));
    }

    @PostMapping("/upload-profile")
    public void addProfilePicture(@RequestParam("file") MultipartFile file, Principal connectedUser){
        imageService.uploadProfileToUserProfile(file,connectedUser);
    }


    @GetMapping("/{email}/profile")
    public ResponseEntity<UserProfileResponse> lookProfile(@PathVariable("email") String email){
        return ResponseEntity.ok(userService.getUserProfile(email));
    }
}
