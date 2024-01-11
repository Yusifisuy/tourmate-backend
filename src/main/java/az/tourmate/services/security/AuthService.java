package az.tourmate.services.security;

import az.tourmate.dtos.security.AuthenticationRequest;
import az.tourmate.dtos.security.AuthenticationResponse;
import az.tourmate.dtos.security.RegisterRequest;
import az.tourmate.exceptions.address.CountryNotFoundException;
import az.tourmate.exceptions.users.UserIsNotFoundException;
import az.tourmate.models.address.Address;
import az.tourmate.models.address.City;
import az.tourmate.models.address.Country;
import az.tourmate.models.files.UserProfile;
import az.tourmate.models.user.UserSession;
import az.tourmate.models.user.UserSessionRequest;
import az.tourmate.models.user.token.Token;
import az.tourmate.models.user.token.TokenType;
import az.tourmate.models.user.User;
import az.tourmate.repositories.address.AddressRepository;
import az.tourmate.repositories.address.CityRepository;
import az.tourmate.repositories.address.CountryRepository;
import az.tourmate.repositories.file.UserProfileRepository;
import az.tourmate.repositories.user.TokenRepository;
import az.tourmate.repositories.user.UserRepository;
import az.tourmate.repositories.user.UserSessionDao;
import az.tourmate.utils.ExceptionTexts;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;
    private final UserProfileRepository profileRepository;
    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;
    private final AddressRepository addressRepository;
    private final UserSessionDao userSessionDao;
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       JwtService jwtService, TokenRepository tokenRepository,
                       AuthenticationManager authenticationManager, UserProfileRepository profileRepository,
                       CountryRepository countryRepository, CityRepository cityRepository, AddressRepository addressRepository, UserSessionDao userSessionDao) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.tokenRepository = tokenRepository;
        this.authenticationManager = authenticationManager;
        this.profileRepository = profileRepository;
        this.countryRepository = countryRepository;
        this.cityRepository = cityRepository;
        this.addressRepository = addressRepository;
        this.userSessionDao = userSessionDao;
    }

    public AuthenticationResponse register(RegisterRequest registerRequest){

        List<UserProfile> userProfiles = new ArrayList<>();
        Country country = countryRepository.findCountryByName(registerRequest.getCountry())
                .orElseThrow(() -> new CountryNotFoundException("Ölkə tapılmadı"));
        City city = cityRepository.findCityByName(registerRequest.getCity())
                .orElseThrow(() -> new CountryNotFoundException("Şəhər tapılmadı"));
        Address address = Address.builder().country(country)
                .city(city).street(registerRequest.getStreet()).build();
        addressRepository.save(address);
        User user = User.builder()
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .email(registerRequest.getEmail())
                .address(address)
                .phoneNumber(registerRequest.getPhoneNumber())
                .citizen(registerRequest.getCitizen())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(registerRequest.getRole())
                .active(true)
                .build();

        User savedUser = userRepository.save(user);

        UserProfile userProfile = createDefaultProfile(savedUser);
        userProfiles.add(userProfile);
        savedUser.setUserProfile(userProfiles);

        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser,jwtToken);
        return AuthenticationResponse.builder().accessToken(jwtToken).refreshToken(refreshToken).build();
    }


    public String registerUser(RegisterRequest registerRequest){
        Country country = countryRepository.findCountryByName(registerRequest.getCountry())
                .orElseThrow(() -> new CountryNotFoundException("Ölkə tapılmadı"));
        City city = cityRepository.findCityByName(registerRequest.getCity())
                .orElseThrow(() -> new CountryNotFoundException("Şəhər tapılmadı"));

        UserSessionRequest sessionRequest =
                UserSessionRequest.builder()
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .email(registerRequest.getEmail())
                .country(registerRequest.getCountry())
                .city(registerRequest.getCity())
                .street(registerRequest.getStreet())
                .phoneNumber(registerRequest.getPhoneNumber())
                .citizen(registerRequest.getCitizen())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(registerRequest.getRole())
                .build();

        userSessionDao.save(new UserSession(sessionRequest.getEmail(),sessionRequest));

        return registerRequest.getEmail();
    }



    public AuthenticationResponse confirmUser(String email){
        List<UserProfile> userProfiles = new ArrayList<>();


        UserSession userSession = userSessionDao.findSessionById(email);

        Country country = countryRepository.findCountryByName(userSession.getSessionRequest().getCountry())
                .orElseThrow(() -> new CountryNotFoundException("Ölkə tapılmadı"));
        City city = cityRepository.findCityByName(userSession.getSessionRequest().getCity())
                .orElseThrow(() -> new CountryNotFoundException("Şəhər tapılmadı"));

        Address address = Address.builder().country(country)
                .city(city).street(userSession.getSessionRequest().getStreet()).build();

        addressRepository.save(address);

        User user = User.builder()
                .firstName(userSession.getSessionRequest().getFirstName())
                .lastName(userSession.getSessionRequest().getLastName())
                .email(userSession.getEmailId())
                .address(address)
                .phoneNumber(userSession.getSessionRequest().getPhoneNumber())
                .citizen(userSession.getSessionRequest().getCitizen())
                .password(passwordEncoder.encode(userSession.getSessionRequest().getPassword()))
                .role(userSession.getSessionRequest().getRole())
                .active(true)
                .build();

        User savedUser = userRepository.save(user);
        userSessionDao.deleteSession(email);
        UserProfile userProfile = createDefaultProfile(savedUser);
        userProfiles.add(userProfile);
        savedUser.setUserProfile(userProfiles);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser,jwtToken);
        return AuthenticationResponse.builder().accessToken(jwtToken).refreshToken(refreshToken).build();
    }


    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getEmail(),
                        authenticationRequest.getPassword()
                )
        );
        User user = userRepository.findUserByEmailAndActiveIsTrue(authenticationRequest.getEmail())
                .orElseThrow(() -> new UserIsNotFoundException(ExceptionTexts.USER_NOT_FOUND));
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return  AuthenticationResponse.builder().accessToken(jwtToken).refreshToken(refreshToken).build();
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private UserProfile createDefaultProfile(User user){
        UserProfile userProfile = UserProfile.builder().fileName("default.png").user(user).active(true).build();
        return profileRepository.save(userProfile);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllByExpiredIsFalseAndRevokedIsFalseAndUser(user);
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            log.info("Token revoked:"+ token.getUser().getEmail());
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }


    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            var user = this.userRepository.findUserByEmailAndActiveIsTrue(userEmail)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
}
