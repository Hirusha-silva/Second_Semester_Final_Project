package com.example.back_end.repo;

import com.example.back_end.dto.UserSummaryDto;
import com.example.back_end.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    @Query("SELECT u.name, u.address, u.email, u.phone FROM User u")
    List<Object[]> findAllUserSummaries();
    //Optional<UserSummaryDto> findUserSummaryById(Long id);

}
