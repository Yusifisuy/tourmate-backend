package az.tourmate.controller.packages;

import az.tourmate.dtos.branches.BranchSortDto;
import az.tourmate.dtos.rooms.SortRoomByPriceRequest;
import az.tourmate.services.packages.SortPackageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/package")
public class SortPackageController {

    private final SortPackageService packageService;

    public SortPackageController(SortPackageService packageService) {
        this.packageService = packageService;
    }


    @PostMapping("/sort-by-city/{cityName}")
    public ResponseEntity<List<BranchSortDto>> sortByCity(@PathVariable("cityName") String cityName){
        return ResponseEntity.ok(packageService.sortBranchesByCity(cityName));
    }

    @PostMapping("/sort-by-price")
    public ResponseEntity<List<BranchSortDto>> sortByPrices(@RequestBody SortRoomByPriceRequest request){
        return ResponseEntity.ok(packageService.sortBranchesByPrice(request.min(),request.max()));
    }
}
