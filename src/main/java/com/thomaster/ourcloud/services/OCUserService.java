package com.thomaster.ourcloud.services;

import com.thomaster.ourcloud.model.user.OCUser;
import com.thomaster.ourcloud.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OCUserService {

    private UserRepository userRepository;

    public OCUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private String getCurrentlyLoggedInUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null)
            return null;

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails) {
            return ((UserDetails)principal).getUsername();
        } else {
            return principal.toString();
        }
    }

    public Optional<OCUser> getCurrentlyLoggedInUser() {
        String username = getCurrentlyLoggedInUsername();
        if (username == null)
            return Optional.empty();

        return Optional.of(userRepository.findByUsername(getCurrentlyLoggedInUsername()));
    }

    public OCUser save(OCUser userToSave) {
        return userRepository.save(userToSave);
    }

    public OCUser findById(Long id) {
        return userRepository.findById(id).get();
    }
}
