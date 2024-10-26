package com.excelr.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.excelr.model.User;

public interface UserRepo extends JpaRepository<User, Long> {

}
