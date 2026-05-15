package it.alemanno.awesome_pizza.repository;

import it.alemanno.awesome_pizza.models.OrderStatus;
import it.alemanno.awesome_pizza.models.PizzaOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PizzaOrderRepository extends JpaRepository<PizzaOrder, Long> {

    Optional<PizzaOrder> findByOrderCode(String orderCode);

    // Trova il primo ordine in stato PENDING ordinato per data di creazione (il più vecchio)
    Optional<PizzaOrder> findFirstByStatusOrderByCreatedAtAsc(OrderStatus status);

}
