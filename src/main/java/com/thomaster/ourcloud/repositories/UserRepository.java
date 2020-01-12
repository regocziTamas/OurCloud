package com.thomaster.ourcloud.repositories;

import com.thomaster.ourcloud.model.OCUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<OCUser, Long> {

    OCUser findByUsername(String username);

}
