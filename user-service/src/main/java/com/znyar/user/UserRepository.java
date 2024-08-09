package com.znyar.user;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmailAndIdIsNot(String email, Long id);

    boolean existsByUserInfo_PhoneAndIdIsNot(String phoneNumber, Long id);

}
