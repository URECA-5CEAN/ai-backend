package com.ureca.ocean.jjh.aibackend.suggestion.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ureca.ocean.jjh.aibackend.suggestion.entity.User;

public interface UserRepository extends JpaRepository<User, UUID>{

}
