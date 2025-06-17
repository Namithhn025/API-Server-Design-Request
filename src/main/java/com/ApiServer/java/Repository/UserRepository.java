package com.ApiServer.java.Repository;

import com.ApiServer.java.Model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {
    User findByUsername(String username);

    boolean existsByUsername(String username);
}