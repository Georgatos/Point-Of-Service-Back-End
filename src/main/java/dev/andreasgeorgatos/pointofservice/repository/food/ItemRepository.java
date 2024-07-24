package dev.andreasgeorgatos.pointofservice.repository.food;

import dev.andreasgeorgatos.pointofservice.model.item.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
}
