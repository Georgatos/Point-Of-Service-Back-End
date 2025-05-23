package dev.andreasgeorgatos.pointofservice.repository.users;

import dev.andreasgeorgatos.pointofservice.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findUserByEmail(@Param("email") String email);

    @Query("SELECT u FROM User u WHERE u.userName = :userName")
    Optional<User> findUserByUsername(String userName);

    @Query("SELECT u.id, u.firstName, u.lastName, u.imageUrl, u.userName, r.name FROM User u INNER JOIN u.roles r WHERE r.name NOT IN ('CUSTOMER', 'ADMIN')")
    Optional<List<Object[]>> getAllEmployees();

}
