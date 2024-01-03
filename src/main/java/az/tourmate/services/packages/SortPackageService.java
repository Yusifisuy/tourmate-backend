package az.tourmate.services.packages;

import az.tourmate.dtos.branches.BranchSortDto;
import az.tourmate.exceptions.file.ProfileIsNotFoundException;
import az.tourmate.exceptions.rooms.RoomIsNotFoundException;
import az.tourmate.models.branches.Branch;
import az.tourmate.models.room.Room;
import az.tourmate.repositories.branch.BranchRepository;
import az.tourmate.repositories.file.BranchProfileRepository;
import az.tourmate.repositories.rooms.RoomRepository;
import az.tourmate.utils.ExceptionTexts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SortPackageService {



    @Value("${application.bucket.branch-profile-link}")
    private String branchProfileLink;

    private final BranchRepository branchRepository;
    private final RoomRepository roomRepository;

    private final BranchProfileRepository profileRepository;

    public SortPackageService(BranchRepository branchRepository, RoomRepository roomRepository, BranchProfileRepository profileRepository) {
        this.branchRepository = branchRepository;
        this.roomRepository = roomRepository;
        this.profileRepository = profileRepository;
    }



    public List<BranchSortDto> sortBranchesByCity(String cityName){

        log.info("log:"+roomRepository.findRoomsByPrice(100.00,470.00));
        List<Branch> branches = branchRepository.findBranchByAddress_City_Name(cityName);
        return branches.stream().map(branch -> new BranchSortDto(branch.getId(),branch.getName()
                ,
                branchProfileLink+profileRepository.findByBranchAndActiveIsTrue(branch)
                        .orElseThrow(()-> new ProfileIsNotFoundException(ExceptionTexts.PROFILE_IS_NOT_FOUND))
                        .getFileName()
                ,getCheapestPrice(branch))).toList();
    }


    public List<BranchSortDto> sortBranchesByPrice(double minPrice,double maxPrice){
        List<Room> rooms = roomRepository.findRoomsByPrice(minPrice,maxPrice)
                .orElseThrow(()->new RoomIsNotFoundException(ExceptionTexts.ROOM_IS_NOT_FOUND));

        List<Branch> branches = rooms.stream().map(room -> room.getBranch()).toList();

        return branches.stream().map(branch -> new BranchSortDto(branch.getId(),branch.getName()
                ,
                branchProfileLink+profileRepository.findByBranchAndActiveIsTrue(branch)
                        .orElseThrow(()-> new ProfileIsNotFoundException(ExceptionTexts.PROFILE_IS_NOT_FOUND))
                        .getFileName()
                ,getCheapestPrice(branch))).toList();
    }



    private double getCheapestPrice(Branch branch) {
        double minimumPrice;

        if (branch.getRooms() == null || branch.getRooms().isEmpty()) {
            log.info("HOTEL HAS NO ROOMS, MINIMUM PRICE SET TO ZERO");
            minimumPrice = 0.0;
        } else {
            minimumPrice = branch.getRooms().get(0).getRoomPrice().getPrice();

            for (int i = 1; i < branch.getRooms().size(); i++) {
                Room room = branch.getRooms().get(i);

                if (room.getRoomPrice().getPrice() < minimumPrice) {
                    log.info("NEW MINIMUM PRICE IS: " + room.getRoomPrice().getPrice());
                    minimumPrice = room.getRoomPrice().getPrice();
                }
            }
        }

        return minimumPrice;
    }


}
