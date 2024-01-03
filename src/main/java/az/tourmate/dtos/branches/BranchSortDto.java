package az.tourmate.dtos.branches;

public record BranchSortDto(

        Long branchId,
        String branchName,
        String profileLink,
        Double cheapestPrice
) {
}
