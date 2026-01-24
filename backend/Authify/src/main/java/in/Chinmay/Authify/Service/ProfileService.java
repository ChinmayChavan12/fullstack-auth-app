package in.Chinmay.Authify.Service;

import in.Chinmay.Authify.IO.ProfileRequest;
import in.Chinmay.Authify.IO.ProfileResponse;


public interface ProfileService {
    ProfileResponse createProfile(ProfileRequest request);
    ProfileResponse getProfile(String email);
    void sendResetOtp(String email);
    void resetPassword(String email,String otp,String newPassword);
    void sendOtp(String email);
    void verifyOtp(String email,String otp);

}
