package com.kolya.gym.repo;

import com.kolya.gym.domain.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends CrudRepository<User,Long> {
    List<User> findAll();

    //@Query(nativeQuery=true, value = "SELECT COUNT(*) FROM usr WHERE username SIMILAR TO CONCAT(:username,'[0-9]*')")
    //Long countDuplicates(@Param("username") String username);

    @Query(nativeQuery=true, value = "SELECT COUNT(*) FROM usr WHERE first_name=:first_name AND last_name=:last_name")
    Long countDuplicates(@Param("first_name") String firstname, @Param("last_name") String lastName);

    User findByUsernameAndPassword(String username, String password);

    Optional<User> findByUsername(String username);
}
