package dev.andreasgeorgatos.tsilikos.repository.users;

import dev.andreasgeorgatos.tsilikos.model.user.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
