package com.chris.cba.chriscba.repository;

import com.chris.cba.chriscba.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long>{

    Boolean existsByAccountNumber(String accountNumber);
    User findByAccountNumber(String accountNumber);
}
