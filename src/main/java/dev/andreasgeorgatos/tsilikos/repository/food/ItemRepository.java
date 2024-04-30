package dev.andreasgeorgatos.tsilikos.repository.food;

import dev.andreasgeorgatos.tsilikos.model.food.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
}
