package in.Chinmay.Authify.Controller;

import in.Chinmay.Authify.IO.ProfileRequest;
import in.Chinmay.Authify.IO.ProfileResponse;
import in.Chinmay.Authify.Service.EmailService;
import in.Chinmay.Authify.Service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;
    private final EmailService emailService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ProfileResponse registerUser(@Valid @RequestBody ProfileRequest request){
           ProfileResponse response= profileService.createProfile(request);
           emailService.sendWelcomeEmail(response.getEmail(),response.getName());
        return response;
    }

    @GetMapping("/profile")
    public ProfileResponse getProfile(@CurrentSecurityContext(expression = "authentication?.name")String email){

        return profileService.getProfile(email);

    }

}
