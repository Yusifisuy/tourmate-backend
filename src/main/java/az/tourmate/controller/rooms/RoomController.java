package az.tourmate.controller.rooms;

import az.tourmate.dtos.rooms.CreateRoomRequest;
import az.tourmate.dtos.rooms.GetRoomDto;
import az.tourmate.services.files.ImageService;
import az.tourmate.services.rooms.RoomService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/rooms")
public class RoomController {

    private final RoomService roomService;
    private final ImageService imageService;

    public RoomController(RoomService roomService, ImageService imageService) {
        this.roomService = roomService;
        this.imageService = imageService;
    }


    @PostMapping("/{branchId}/create-room")
    @PreAuthorize("hasAnyAuthority('owner:create','manager:create','admin:create')")
    public boolean createRoom(@RequestBody CreateRoomRequest roomRequest,
                              @PathVariable("branchId")Long branchId, Principal connectedUser){
        return roomService.createRoomForBranch(roomRequest,connectedUser,branchId);
    }

    @GetMapping("/{roomId}")
    public GetRoomDto getRoom(@PathVariable("roomId") Long roomId){
        return roomService.getRoom(roomId);
    }


    @PostMapping("/{roomId}/add-image")
    @PreAuthorize("hasAnyAuthority('owner:create','manager:create','admin:create')")
    public String addImageToRoom(@RequestParam("file") MultipartFile file,
                                 Principal connectedUser,@PathVariable("roomId") Long roomId){
       return imageService.uploadFileToRooms(file,connectedUser,roomId);
    }
}
