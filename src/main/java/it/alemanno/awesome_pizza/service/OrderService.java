package it.alemanno.awesome_pizza.service;

import it.alemanno.awesome_pizza.models.OrderStatus;
import it.alemanno.awesome_pizza.models.PizzaOrder;
import it.alemanno.awesome_pizza.repository.PizzaOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final PizzaOrderRepository repository;

    // --- CUSTOMER LOGIC ---

    public PizzaOrder createOrder(String pizzaType) {
        PizzaOrder order = new PizzaOrder();
        // Generate a unique 8-character alphanumeric code
        order.setOrderCode(UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        order.setPizzaType(pizzaType);
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());
        return repository.save(order);
    }

    public Optional<PizzaOrder> getOrderByCode(String orderCode) {
        return repository.findByOrderCode(orderCode);
    }

    // --- PIZZA MAKER LOGIC ---

    public Optional<PizzaOrder> takeNextOrder() {
        Optional<PizzaOrder> nextOrder = repository.findFirstByStatusOrderByCreatedAtAsc(OrderStatus.PENDING);
        if (nextOrder.isPresent()) {
            PizzaOrder order = nextOrder.get();
            order.setStatus(OrderStatus.IN_PROGRESS);
            return Optional.of(repository.save(order));
        }
        return Optional.empty();
    }

    public Optional<PizzaOrder> completeOrder(Long orderId) {
        Optional<PizzaOrder> orderOpt = repository.findById(orderId);
        if (orderOpt.isPresent() && orderOpt.get().getStatus() == OrderStatus.IN_PROGRESS) {
            PizzaOrder order = orderOpt.get();
            order.setStatus(OrderStatus.COMPLETED);
            return Optional.of(repository.save(order));
        }
        return Optional.empty();
    }

    public long getTotalOrders() {
        return repository.count();
    }
}
