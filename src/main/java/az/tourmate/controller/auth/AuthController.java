package az.tourmate.controller.auth;

import az.tourmate.dtos.security.AuthenticationRequest;
import az.tourmate.dtos.security.AuthenticationResponse;
import az.tourmate.dtos.security.RegisterRequest;
import az.tourmate.services.security.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/v2/sign-up")
    public ResponseEntity<String> signUpUser(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(authService.registerUser(request));
    }

    @PostMapping("/confirm/{email}")
    public ResponseEntity<AuthenticationResponse> confirmUser(@PathVariable("email") String email){
        return ResponseEntity.ok(authService.confirmUser(email));
    }


    @PostMapping("/sign-up")
    public ResponseEntity<AuthenticationResponse> signUp(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request){
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        authService.refreshToken(request, response);
    }


}
