package com.example.user_registration_OTP.Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.user_registration_OTP.Models.UserModel;
import com.example.user_registration_OTP.Repositories.AppUserRepository;

@Service
public class AppUserService implements UserDetailsService {

    @Autowired
    private AppUserRepository appUserRepository;



   



    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserModel appUser = appUserRepository.findByEmail(email).get();
        if(appUser != null) {
            var springUser = User.withUsername(appUser.getEmail())
                    .password(appUser.getPassword())
                    
                    .build();
                    return springUser;
        } 
        return null;
    }



  

   
    
    
}
