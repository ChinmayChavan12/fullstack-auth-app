package in.Chinmay.Authify.Service;

import in.Chinmay.Authify.IO.ProfileRequest;
import in.Chinmay.Authify.IO.ProfileResponse;
import in.Chinmay.Authify.Model.UserEntity;
import in.Chinmay.Authify.Repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    private final UserEntityRepository userRepository;
    @Override
    public ProfileResponse createProfile(ProfileRequest request) {
        UserEntity newProfile=convertToUserEntity(request);
        if(!userRepository.existsByEmail(newProfile.getEmail())) {
            newProfile=userRepository.save(newProfile);
            return convertToProfileResponse(newProfile);
        }

        throw   new ResponseStatusException(HttpStatus.CONFLICT,"Profile with given email: "+request.getEmail()+" already exists");

    }

    private ProfileResponse convertToProfileResponse(UserEntity newProfile) {
        return ProfileResponse.builder()
                .name(newProfile.getName())
                .userId(newProfile.getUserId())
                .email(newProfile.getEmail())
                .isVerified(newProfile.getIsAccountVerified())
                .build();
    }

    private UserEntity convertToUserEntity(ProfileRequest request) {
            return UserEntity.builder()
                    .userId(UUID.randomUUID().toString())
                    .name(request.getName())
                    .password(request.getPassword())
                    .email(request.getEmail())
                    .verifyOtp(null)
                    .isAccountVerified(false)
                    .verifyOtpExpirationAt(0L)
                    .resetOtp(null)
                    .resetOtpExpirationAt(0L)
                    .build();
    }
}
