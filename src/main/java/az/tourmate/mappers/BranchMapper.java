package az.tourmate.mappers;

import az.tourmate.dtos.branches.BranchGetDto;
import az.tourmate.dtos.branches.BranchSortDto;
import az.tourmate.models.branches.Branch;
import az.tourmate.models.room.Room;
import az.tourmate.models.scores.Score;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.stream.Collectors;

public class BranchMapper {

    @Value("${application.bucket.branch-profile-link}")
    private static String branchProfileLink;

    public static List<BranchGetDto> mapBranchListToDto(List<Branch> branches,String fileName){
        return branches.stream().map(branch ->
                new BranchGetDto(branch.getCapacity(),
                        branch.getCommonInfoAboutBranch(),
                        branch.getCommonInfoAboutPlace(),
                        branch.getName(),
                        branch.getManagement().getManagementName(),
                        countAverage(branch),
                        CommentMapper.mapBranchCommentListToDto(branch.getBranchComments(),fileName),
                        ImageMapper.mapImageListToDto(branch.getBranchImages()),
                        RoomMapper.mapRoomListToDto(branch.getRooms()))).collect(Collectors.toList());
    }


    public static List<BranchSortDto> mapBranchListToSortDto(List<Branch> branches,String fileName){
        return branches.stream().map(branch -> new BranchSortDto(branch.getId(),branch.getName()
                ,branchProfileLink+branch,getCheapestPrice(branch))).toList();

    }


    private static double countAverage(Branch branch){
        double averageScore = 0;
        for (Score score : branch.getScores()){
            averageScore+=score.getScore();
        }
        return averageScore/branch.getScores().size();
    }


    private static double getCheapestPrice(Branch branch){
        double minimumPrice = branch.getRooms().get(0).getRoomPrice().getPrice();
        for (Room room: branch.getRooms()){
            if (room.getRoomPrice().getPrice()<minimumPrice){
                minimumPrice=room.getRoomPrice().getPrice();
            }
        }
        return minimumPrice;
    }
}
