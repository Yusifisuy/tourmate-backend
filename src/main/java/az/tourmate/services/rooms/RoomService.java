package az.tourmate.services.rooms;

import az.tourmate.dtos.rooms.CreateRoomRequest;
import az.tourmate.dtos.rooms.GetRoomDto;
import az.tourmate.dtos.rooms.RoomPriceDto;
import az.tourmate.exceptions.branch.BranchIsNotFoundException;
import az.tourmate.exceptions.rooms.RoomIsNotFoundException;
import az.tourmate.exceptions.users.UserHasNotAccessException;
import az.tourmate.mappers.ImageMapper;
import az.tourmate.models.branches.Branch;
import az.tourmate.models.room.Room;
import az.tourmate.models.room.RoomPrice;
import az.tourmate.models.user.User;
import az.tourmate.repositories.branch.BranchRepository;
import az.tourmate.repositories.rooms.RoomPriceRepository;
import az.tourmate.repositories.rooms.RoomRepository;
import az.tourmate.utils.ExceptionTexts;
import az.tourmate.utils.UserUtil;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final BranchRepository branchRepository;
    private final RoomPriceRepository priceRepository;

    public RoomService(RoomRepository roomRepository, BranchRepository branchRepository, RoomPriceRepository priceRepository) {
        this.roomRepository = roomRepository;
        this.branchRepository = branchRepository;
        this.priceRepository = priceRepository;
    }


    public boolean createRoomForBranch(CreateRoomRequest roomRequest, Principal connectedUser,Long branchId){
        User user = UserUtil.getConnectedUser(connectedUser);
        Branch branch = branchRepository.findByIdAndActiveIsTrue(branchId)
                .orElseThrow(() -> new BranchIsNotFoundException(ExceptionTexts.BRANCH_NOT_FOUND));

        if (checkIfUserHasAccessToBranch(user,branch)){
            RoomPrice roomPrice = RoomPrice.builder()
                    .price(roomRequest.price()).breakFast(roomRequest.breakfastInclude()).refund(roomRequest.refund())
                    .build();
            priceRepository.save(roomPrice);
            Room room = Room.builder().name(roomRequest.name())
                    .infoAboutRoom(roomRequest.info())
                    .count(roomRequest.count()).activeCount(roomRequest.count()).capacity(roomRequest.capacity())
                    .roomType(roomRequest.type()).features(roomRequest.features()).branch(branch).roomPrice(roomPrice).active(true).build();

            roomRepository.save(room);
            return true;
        }
        else {
            throw new UserHasNotAccessException(ExceptionTexts.ACCESS_DENIED);
        }
    }



    public GetRoomDto getRoom(Long roomId){
        Room room = roomRepository.findByIdAndActiveIsTrue(roomId)
                .orElseThrow(()-> new RoomIsNotFoundException(ExceptionTexts.ROOM_IS_NOT_FOUND));
        RoomPriceDto priceDto =
                new RoomPriceDto(room.getRoomPrice().getPrice(),room.getRoomPrice().isBreakFast(),room.getRoomPrice().isRefund());
        return new GetRoomDto(room.getId(),ImageMapper.mapRoomImageListToDto(
                room.getRoomImages()),room.getName(),room.getInfoAboutRoom(),room.getFeatures(),priceDto);
    }



    private boolean checkIfUserHasAccessToBranch(User user, Branch branch){
        return user.getManagement().getId().equals(branch.getManagement().getId());
    }


}
