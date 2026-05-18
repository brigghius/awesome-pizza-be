package it.alemanno.awesome_pizza.service;

import it.alemanno.awesome_pizza.models.OrderStatus;
import it.alemanno.awesome_pizza.models.PizzaOrder;
import it.alemanno.awesome_pizza.repository.PizzaOrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private PizzaOrderRepository repository;

    @InjectMocks
    private OrderService orderService;

    @Test
    void testCreateOrder() {
        // Arrange
        PizzaOrder savedOrder = new PizzaOrder();
        savedOrder.setPizzaType("Margherita");
        savedOrder.setStatus(OrderStatus.PENDING);
        savedOrder.setOrderCode("ABC12345");

        when(repository.save(any(PizzaOrder.class))).thenReturn(savedOrder);

        // Act
        PizzaOrder result = orderService.createOrder("Margherita");

        // Assert
        assertNotNull(result);
        assertEquals(OrderStatus.PENDING, result.getStatus());
        assertEquals("ABC12345", result.getOrderCode());
        verify(repository, times(1)).save(any(PizzaOrder.class));
    }

    @Test
    void testTakeNextOrder() {
        // Arrange
        PizzaOrder pendingOrder = new PizzaOrder();
        pendingOrder.setId(1L);
        pendingOrder.setStatus(OrderStatus.PENDING);

        when(repository.findFirstByStatusOrderByCreatedAtAsc(OrderStatus.PENDING))
                .thenReturn(Optional.of(pendingOrder));
        when(repository.save(any(PizzaOrder.class))).thenReturn(pendingOrder); // Il mock restituisce lo stesso oggetto salvato

        // Act
        Optional<PizzaOrder> result = orderService.takeNextOrder();

        // Assert
        assertTrue(result.isPresent());
        assertEquals(OrderStatus.IN_PROGRESS, result.get().getStatus());
        verify(repository, times(1)).save(pendingOrder);
    }

    @Test
    void testCountOrdersNotCompleted() {
        // Arrange
        long notCompletedOrders = 5L;
        when(repository.countOrdersNotCompleted()).thenReturn(notCompletedOrders);

        // Act
        long result = orderService.countOrdersNotCompleted();

        // Assert
        assertEquals(notCompletedOrders, result);
        verify(repository, times(1)).countOrdersNotCompleted();
    }

}
