package it.alemanno.awesome_pizza.controller;

import it.alemanno.awesome_pizza.models.PizzaOrder;
import it.alemanno.awesome_pizza.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class OrderController {
    private final OrderService orderService;

    // === CUSTOMER API ===

    @PostMapping("/customer/orders")
    public ResponseEntity<PizzaOrder> placeOrder(@RequestBody String pizzaType) {
        PizzaOrder order = orderService.createOrder(pizzaType);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/customer/orders/{orderCode}")
    public ResponseEntity<PizzaOrder> checkOrderStatus(@PathVariable String orderCode) {
        return orderService.getOrderByCode(orderCode)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // === PIZZA MAKER API ===

    @PutMapping("/chef/orders/take-next")
    public ResponseEntity<PizzaOrder> takeNextOrder() {
        return orderService.takeNextOrder()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build()); // Ritorna 204 se non ci sono ordini
    }

    @PutMapping("/chef/orders/{id}/complete")
    public ResponseEntity<PizzaOrder> completeOrder(@PathVariable Long id) {
        return orderService.completeOrder(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }


}
