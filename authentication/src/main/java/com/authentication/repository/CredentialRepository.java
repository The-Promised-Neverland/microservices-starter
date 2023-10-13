package com.authentication.repository;

import com.authentication.module.UserCredentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CredentialRepository extends JpaRepository<UserCredentials,Long> {

    UserCredentials findByEmail(String email);
}
