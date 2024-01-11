package az.tourmate.models.user;

import az.tourmate.models.address.Address;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSessionRequest implements Serializable {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;
    private String country;
    private String city;
    private String street;
    private String citizen;
    private Role role;


    @Override
    public String toString() {
        return "UserSessionRequest{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", street='" + street + '\'' +
                ", citizen='" + citizen + '\'' +
                ", role=" + role +
                '}';
    }
}
