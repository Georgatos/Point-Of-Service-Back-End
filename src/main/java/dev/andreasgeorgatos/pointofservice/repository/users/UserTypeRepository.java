package dev.andreasgeorgatos.pointofservice.repository.users;

import dev.andreasgeorgatos.pointofservice.model.user.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTypeRepository extends JpaRepository<UserType, Long> {
}
