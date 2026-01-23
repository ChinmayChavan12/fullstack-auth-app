package in.Chinmay.Authify.IO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileRequest {
    @NotBlank(message = "Name Should not be Blank")
    private String name;
    @Email(message = "Enter a Valid Email")
    @NotNull(message = "email should not be null")
    private String email;
    @Size(min=6,message = "Enter a correct Password atleast 6 characters")
    private String password;
}
