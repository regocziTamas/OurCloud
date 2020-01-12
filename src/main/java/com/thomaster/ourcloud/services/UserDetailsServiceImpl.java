package com.thomaster.ourcloud.services;

import com.thomaster.ourcloud.model.OCUser;
import com.thomaster.ourcloud.repositories.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepository ocUserRepository;

    public UserDetailsServiceImpl(UserRepository ocUserRepository) {
        this.ocUserRepository = ocUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        OCUser applicationUser = ocUserRepository.findByUsername(username);

        if (applicationUser == null) {
            throw new UsernameNotFoundException(username);
        }

        return User.builder()
                .username(applicationUser.getUsername())
                .password(applicationUser.getPassword())
                .roles(applicationUser.getRole().toString())
                .build();
    }


}
