package az.tourmate.mappers;

import az.tourmate.dtos.images.BranchImageDto;
import az.tourmate.dtos.images.RoomImageDto;
import az.tourmate.models.files.BranchImage;
import az.tourmate.models.room.RoomImage;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

public class ImageMapper {

    @Value("${application.bucket.name-link}")
    private static String branchImageLink;

    @Value("${application.bucket.rooms-link}")
    private static String roomImageLink;


    public static List<BranchImageDto> mapImageListToDto(List<BranchImage> imageList){
        return imageList.stream().map(image -> new BranchImageDto("https://tourmate.s3.eu-central-1.amazonaws.com/"+image.getFileName())).toList();
    }

    public static List<RoomImageDto> mapRoomImageListToDto(List<RoomImage> images){
        return images.stream().map(image -> new RoomImageDto("https://tourmaterooms.s3.eu-central-1.amazonaws.com/"+image.getFileName())).toList();
    }
}
