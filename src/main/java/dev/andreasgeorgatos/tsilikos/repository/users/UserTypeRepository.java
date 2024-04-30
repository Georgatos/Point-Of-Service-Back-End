package dev.andreasgeorgatos.tsilikos.repository.users;

import dev.andreasgeorgatos.tsilikos.model.user.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTypeRepository extends JpaRepository<UserType, Long> {
}
