package az.tourmate.dtos.branches;

import az.tourmate.dtos.comments.BranchCommentGetDto;
import az.tourmate.dtos.images.BranchImageDto;
import az.tourmate.dtos.rooms.GetRoomDto;

import java.util.List;

public record BranchGetDto(
        Long capacity,
        String commonInfoAboutBranch,
        String commonInfoAboutPlace,
        String name,
        String managementName,
        Double score,
        List<BranchCommentGetDto> comments,
        List<BranchImageDto> images,
        List<GetRoomDto> rooms

) {
}
