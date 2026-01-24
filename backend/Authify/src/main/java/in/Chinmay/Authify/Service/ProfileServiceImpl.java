package in.Chinmay.Authify.Service;

import in.Chinmay.Authify.IO.ProfileRequest;
import in.Chinmay.Authify.IO.ProfileResponse;
import in.Chinmay.Authify.Model.UserEntity;
import in.Chinmay.Authify.Repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailSender;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    private final UserEntityRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    @Override
    public ProfileResponse createProfile(ProfileRequest request) {
        UserEntity newProfile=convertToUserEntity(request);
        if(!userRepository.existsByEmail(newProfile.getEmail())) {
            newProfile=userRepository.save(newProfile);
            return convertToProfileResponse(newProfile);
        }

        throw   new ResponseStatusException(HttpStatus.CONFLICT,"Profile with given email: "+request.getEmail()+" already exists");

    }

    @Override
    public ProfileResponse getProfile(String email) {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(()->new UsernameNotFoundException("User not found"+email));
        return convertToProfileResponse(userEntity);
    }

    @Override
    public void sendResetOtp(String email) {
        UserEntity existingEntity= userRepository.findByEmail(email)
                .orElseThrow(()->new UsernameNotFoundException("User not found"+email));
        //Generate otp
         String otp=String.valueOf(ThreadLocalRandom.current().nextInt(100000,1000000));
         //calculate experiy time
         long expiryTime =System.currentTimeMillis()+(15*60*1000);

         //update the profile enity

        existingEntity.setResetOtp(otp);
        existingEntity.setResetOtpExpirationAt(expiryTime);

        userRepository.save(existingEntity);

        try{
            //Todo:send the rest otp main
            emailService.sendResetOtp(existingEntity.getEmail(),otp);

        }catch(Exception e){
            throw new RuntimeException("Unable To send Email");
        }
    }

    @Override
    public void resetPassword(String email, String otp, String newPassword) {
        UserEntity existingEnity=userRepository.findByEmail(email)
                .orElseThrow(()->new UsernameNotFoundException("User not found"+email));
        if(existingEnity.getResetOtp()==null || !existingEnity.getResetOtp().equals(otp)){
            throw new RuntimeException("OTP is not valid");
        }
        if(existingEnity.getResetOtpExpirationAt()<System.currentTimeMillis()){
            throw new RuntimeException("OTP expired");
        }
        existingEnity.setPassword(passwordEncoder.encode(newPassword));
        existingEnity.setResetOtp(null);
        existingEnity.setResetOtpExpirationAt(0L);
        userRepository.save(existingEnity);
    }

    @Override
    public void sendOtp(String email) {
        UserEntity existingUser= userRepository.findByEmail(email)
                .orElseThrow(()->new UsernameNotFoundException("User not found"+email));

        if(existingUser.getIsAccountVerified()!=null&& existingUser.getIsAccountVerified()){
            return;
        }
        String otp=String.valueOf(ThreadLocalRandom.current().nextInt(100000,1000000));
        Long expiryTime =System.currentTimeMillis()+(24*60*60*1000);

        existingUser.setVerifyOtp(otp);
        existingUser.setVerifyOtpExpirationAt(expiryTime);

        userRepository.save(existingUser);
        try{
            emailService.sendVerifyOtp(existingUser.getEmail(),otp);
        }catch(Exception e){
            throw new RuntimeException("Unable To send Email");
        }
    }

    @Override
    public void verifyOtp(String email, String otp) {
            UserEntity existingUser= userRepository.findByEmail(email)
                   .orElseThrow(()->new UsernameNotFoundException("User not found"+email));
            if(existingUser.getVerifyOtp()==null || !existingUser.getVerifyOtp().equals(otp)){
                throw new RuntimeException("OTP is not valid");
            }
            if(existingUser.getVerifyOtpExpirationAt()<System.currentTimeMillis()){
                throw new RuntimeException("OTP expired");
            }
            existingUser.setIsAccountVerified(true);
            existingUser.setVerifyOtp(null);
            existingUser.setVerifyOtpExpirationAt(0L);
            userRepository.save(existingUser);
            try{
                emailService.sendAccountVerifiedMail(email);
            }catch(Exception e){
                throw new RuntimeException("Unable To send Email");
            }
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
                    .password(passwordEncoder.encode(request.getPassword()))
                    .email(request.getEmail())
                    .verifyOtp(null)
                    .isAccountVerified(false)
                    .verifyOtpExpirationAt(0L)
                    .resetOtp(null)
                    .resetOtpExpirationAt(0L)
                    .build();
    }
}
