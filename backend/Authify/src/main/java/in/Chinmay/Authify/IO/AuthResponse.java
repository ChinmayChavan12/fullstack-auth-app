package in.Chinmay.Authify.IO;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String email;
}
