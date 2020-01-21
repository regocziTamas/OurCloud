package com.thomaster.ourcloud.bootstrap;

import com.thomaster.ourcloud.model.OCUser;
import com.thomaster.ourcloud.model.Role;
import com.thomaster.ourcloud.repositories.UserRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class UserBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private UserRepository userRepository;

    public UserBootstrap(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        OCUser user = new OCUser();

        user.setUsername("Thomaster");

        user.setPassword("$2a$10$.8nhpmny.b3DSfYabD1dle1EK4OeV1n.EdTrvpAXqqiArqpQm8LoG");
        user.setRole(Role.ADMIN);

        userRepository.save(user);
    }
}
