package in.Chinmay.Authify.IO;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordRequest {
    @NotBlank(message = "Email should not be blank" )
    private String email;
    @NotBlank(message = "Otp should not be blank" )
    private String otp;
    @NotBlank(message = "Password should not be blank" )
    private String newPassword;
}
