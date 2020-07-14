package com.poppulo.lottery.repository;

import com.poppulo.lottery.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {


    User findByUserName(String userName);
}
