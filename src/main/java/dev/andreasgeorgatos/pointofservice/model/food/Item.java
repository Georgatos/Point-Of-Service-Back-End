package dev.andreasgeorgatos.pointofservice.model.food;

import dev.andreasgeorgatos.pointofservice.enums.Allergenes;
import dev.andreasgeorgatos.pointofservice.enums.CategoryType;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Data
@Table(name = "foods")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "isVegan", nullable = false)
    private boolean isVegan;

    @Column(name = "item_name", nullable = false, length = 100)
    private String itemName;

    @Column(name = "category", nullable = false, length = 500)
    private Set<CategoryType> category;

    @Column(name = "item_description", nullable = false, length = 300)
    private String itemDescription;

    @Column(name = "allergenes", nullable = true, length = 500)
    private Set<Allergenes> allergenes;

    @Column(name = "item_price", nullable = false, precision = 2)
    private double itemPrice;

    @Column(name = "image")
    private String imageURI;
}
