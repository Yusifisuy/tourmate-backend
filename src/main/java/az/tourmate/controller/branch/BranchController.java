package az.tourmate.controller.branch;

import az.tourmate.dtos.branches.BranchGetDto;
import az.tourmate.dtos.branches.CreateBranchDto;
import az.tourmate.services.branches.BranchService;
import az.tourmate.services.files.ImageService;
import az.tourmate.services.score.ScoreService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/branch")
public class BranchController {

    private final BranchService branchService;
    private final ScoreService scoreService;
    private final ImageService imageService;

    public BranchController(BranchService branchService, ScoreService scoreService,ImageService imageService) {
        this.branchService = branchService;
        this.scoreService = scoreService;
        this.imageService = imageService;
    }

    @PostMapping("/{hotelId}/create-branch")
    @PreAuthorize("hasAnyAuthority('owner:create','manager:create')")
    public ResponseEntity<BranchGetDto>
    createBranch(@RequestBody CreateBranchDto branchDto, @PathVariable("hotelId") Long hotelId, Principal connectedUser){
        return ResponseEntity.ok(branchService.createBranch(branchDto,hotelId,connectedUser));
    }


    @PostMapping("/{branchId}/favorite")
    public ResponseEntity<Boolean> favoriteBranch(@PathVariable("branchId") Long branchId, Principal connectedUser){
        return ResponseEntity.ok(branchService.addToFavorites(connectedUser,branchId));
    }


    @PostMapping("/{branchId}/give-star")
    public void giveStar(@PathVariable("branchId") Long branchId,@RequestBody Double score, Principal connectedUser){
        scoreService.giveScore(branchId,connectedUser,score);
    }

    @PreAuthorize("hasAuthority('owner:delete')")
    @DeleteMapping("/{branchId}/delete-branch")
    public void deleteBranch(@PathVariable("branchId") Long branchId,Principal connectedUser){
        branchService.deleteBranch(connectedUser,branchId);
    }


    @GetMapping("/{branchId}")
    public ResponseEntity<BranchGetDto> getBranch(@PathVariable("branchId") Long branchId){
        return ResponseEntity.ok(branchService.getBranch(branchId));
    }

    @PostMapping("/{branchId}/profile")
    @PreAuthorize("hasAnyAuthority('owner:create','manager:create')")
    public ResponseEntity<String> uploadProfile(@RequestBody MultipartFile file,
                                                Principal connectedUser, @PathVariable("branchId")Long branchId){
        return ResponseEntity.ok(imageService.uploadFileToBranchProfile(file,connectedUser,branchId));
    }


    @PostMapping("/{branchId}/closeBranch")
    @PreAuthorize("hasAnyAuthority('owner:create','manager:create')")
    public void closeBranch(Principal connectedUser,@PathVariable("branchId") Long branchId){
         branchService.closeBranch(connectedUser,branchId);
    }



}
