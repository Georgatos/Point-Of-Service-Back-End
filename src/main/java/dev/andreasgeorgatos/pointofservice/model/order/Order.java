package dev.andreasgeorgatos.pointofservice.model.order;

import dev.andreasgeorgatos.pointofservice.model.item.Item;
import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "order_date", nullable = false)
    private LocalDate orderDate;

    @ManyToOne
    @JoinColumn(name = "order_status_id")
    private OrderStatuses orderStatusId;

    @ManyToOne
    @JoinColumn(name = "order_types_id")
    private OrderType orderTypeId;

    @Column(name = "order_total", nullable = false)
    @PositiveOrZero(message = "The cost of the order can be only positive or zero.")
    private Double orderTotal;

    @ManyToMany
    @JoinTable(
            name = "order_items",
            joinColumns = @JoinColumn(name = "order_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "item_id", referencedColumnName = "id")
    )
    @Fetch(FetchMode.JOIN)
    @Size(min = 1)
    private List<Item> items;

}
