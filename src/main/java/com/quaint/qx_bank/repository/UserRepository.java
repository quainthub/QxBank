package com.quaint.qx_bank.repository;

import com.quaint.qx_bank.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
