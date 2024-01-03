package az.tourmate.controller.management;

import az.tourmate.dtos.hotel.CreateManagementRequest;
import az.tourmate.dtos.hotel.ManagementGetDto;
import az.tourmate.services.files.ImageService;
import az.tourmate.services.management.ManagementService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/management")
@PreAuthorize("hasRole('OWNER')")
public class ManagementController {

    private final ManagementService managementService;
    private final ImageService imageService;

    public ManagementController(ManagementService managementService, ImageService imageService) {
        this.managementService = managementService;
        this.imageService = imageService;
    }


    @PostMapping("/createHotel")
    @PreAuthorize("hasAuthority('owner:create')")
    public ResponseEntity<ManagementGetDto> createHotel(@RequestBody CreateManagementRequest hotelDto, Principal connectedUser){
        return ResponseEntity.ok(managementService.createManagement(hotelDto,connectedUser));
    }


    @PostMapping("/{managementId}/profile")
    public ResponseEntity<String> uploadProfile(@RequestBody MultipartFile file,
                                                Principal connectedUser, @PathVariable("managementId")Long managementId){
        return ResponseEntity.ok(imageService.uploadFileToManagementProfile(file,connectedUser,managementId));
    }
}
