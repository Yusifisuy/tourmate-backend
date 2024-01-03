package az.tourmate.dtos.branches;

public record CreateBranchDto(
        String branchName,
        Long capacity,
        String commonInfoAboutBranch,
        String commonInfoAboutPlace,
        String countryName,
        String cityName,
        String street

) {
}