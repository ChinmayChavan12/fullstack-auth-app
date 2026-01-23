package in.Chinmay.Authify.Service;

import in.Chinmay.Authify.IO.ProfileRequest;
import in.Chinmay.Authify.IO.ProfileResponse;


public interface ProfileService {
    ProfileResponse createProfile(ProfileRequest request);
}
