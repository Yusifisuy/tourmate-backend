package az.tourmate.controller.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
@Slf4j
public class AdminController {

    @GetMapping("/check")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<String> checkAdmin(){
        log.info("ADMIN CAME");
        return ResponseEntity.ok("you are admin!");
    }
}
