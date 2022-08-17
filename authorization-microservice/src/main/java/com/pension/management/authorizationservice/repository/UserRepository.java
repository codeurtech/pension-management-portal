package com.pension.management.authorizationservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pension.management.authorizationservice.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

}
