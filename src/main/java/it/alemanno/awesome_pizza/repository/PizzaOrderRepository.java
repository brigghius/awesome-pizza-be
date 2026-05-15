package it.alemanno.awesome_pizza.repository;

import it.alemanno.awesome_pizza.models.OrderStatus;
import it.alemanno.awesome_pizza.models.PizzaOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PizzaOrderRepository extends JpaRepository<PizzaOrder, Long> {

    Optional<PizzaOrder> findByOrderCode(String orderCode);

    // Find the first order in PENDING status sorted by creation date (oldest)
    Optional<PizzaOrder> findFirstByStatusOrderByCreatedAtAsc(OrderStatus status);

}
