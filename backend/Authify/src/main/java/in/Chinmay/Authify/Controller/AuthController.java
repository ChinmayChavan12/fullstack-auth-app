package in.Chinmay.Authify.Controller;

import in.Chinmay.Authify.IO.AuthRequest;
import in.Chinmay.Authify.IO.AuthResponse;
import in.Chinmay.Authify.IO.ResetPasswordRequest;
import in.Chinmay.Authify.Service.AppUserDetailsService;
import in.Chinmay.Authify.Service.ProfileService;
import in.Chinmay.Authify.Util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final AppUserDetailsService appUserDetailsService;
    private final JwtUtil jwtUtil;
    private final ProfileService profileService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        try{
            authenticate(authRequest.getEmail(),authRequest.getPassword());
            final UserDetails userDetails= appUserDetailsService.loadUserByUsername(authRequest.getEmail());
            final String jwtToken= jwtUtil.generateToken(userDetails);
            ResponseCookie cookie= ResponseCookie.from("jwt",jwtToken)
                    .httpOnly(true)
                    .path("/")
                    .maxAge(Duration.ofDays(1))
                    .sameSite("Strict")
                    .build();
                return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,cookie.toString())
                        .body(new AuthResponse(jwtToken,authRequest.getEmail()));
        }catch (BadCredentialsException e){
            Map<String, Object> error = new HashMap<>();
            error.put("error",true);
            error.put("message","Incorrect Email or Password");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }catch (DisabledException e){
            Map<String, Object> error = new HashMap<>();
            error.put("error",true);
            error.put("message","USER_DISABLED");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }catch (Exception e){
            Map<String, Object> error = new HashMap<>();
            error.put("error",true);
            error.put("message","Authentication Failed");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }

    private void authenticate(String email, String password) {

    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

    }
    @PostMapping("/send-reset-otp")
    public void sendResetOtp(@RequestParam String email) {
        try{
            profileService.sendResetOtp(email);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage());
        }
    }

    @GetMapping("/is-authenticated")
    public ResponseEntity<Boolean> isAuthenticated(@CurrentSecurityContext(expression = "authentication?.name")String email) {

        return ResponseEntity.ok(email!=null);
    }
    @PostMapping("/reset-password")
    public void resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {

        try{
            profileService.resetPassword(resetPasswordRequest.getEmail(), resetPasswordRequest.getOtp(), resetPasswordRequest.getNewPassword());

        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage());
        }

    }
    @PostMapping("/send-otp")
    public void sendVerifyOtp(@CurrentSecurityContext(expression ="authentication?.name" )String email) {
        try{
            profileService.sendOtp(email);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage());
        }
    }
    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyEmail(
            @RequestBody Map<String, Object> request,
            @CurrentSecurityContext(expression = "authentication?.name") String email
    ) {
        Object otpObj = request.get("otp");

        if (otpObj == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "OTP is required");
        }

        String otp = otpObj.toString();

        try {
            profileService.verifyOtp(email, otp);
            return ResponseEntity.ok("OTP verified successfully");
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "OTP verification failed");
        }
    }
        @PostMapping("/logout")
        public ResponseEntity<?> logout (HttpServletRequest request) {

            ResponseCookie cookie = ResponseCookie.from("jwt","")
                    .maxAge(0)
                    .httpOnly(true)
                    .secure(false)
                    .path("/")
                    .sameSite("Strict").build();


                return  ResponseEntity.ok().header(HttpHeaders.SET_COOKIE.toString()).body("Logout out Successfully");
        }

}
