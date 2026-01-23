package in.Chinmay.Authify.Service;

import in.Chinmay.Authify.Model.UserEntity;
import in.Chinmay.Authify.Repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

    private final UserEntityRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
       UserEntity existingUser= userRepository.findByEmail(email)
               .orElseThrow(() -> new UsernameNotFoundException(email));

       return new User(existingUser.getEmail(), existingUser.getPassword(), new ArrayList<>());
    }
}
