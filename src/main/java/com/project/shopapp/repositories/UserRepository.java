package com.project.shopapp.repositories;

import com.project.shopapp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Checks if a User exists with the given phone number.
     *
     * @param phoneNumber the phone number to check
     * @return true if a User exists with the given phone number, false otherwise
     */
    boolean existsByPhoneNumber(String phoneNumber);

    /**
     * Finds a User by phone number.
     *
     * @param phoneNumber the phone number to search by
     * @return an Optional containing the User if found, or Optional.empty() if not
     */
    Optional<User> findByPhoneNumber(String phoneNumber);

    /**
     * Finds a User by email.
     *
     * @param email the email to search by
     * @return an Optional containing the User if found, or Optional.empty() if not
     */
    Optional<User> findByEmail(String email);

}
