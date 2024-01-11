package az.tourmate.controller.order;

import az.tourmate.dtos.order.MakeOrderRequest;
import az.tourmate.models.order.Session;
import az.tourmate.services.order.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/check-order/")
    public ResponseEntity<String> checkOrder(@RequestBody MakeOrderRequest orderRequest, Principal connectedUser){
        return ResponseEntity.ok(orderService.order(orderRequest.roomId(),
                orderRequest.count(),
                orderRequest.entryDate(),
                orderRequest.outDate(),
                connectedUser));
    }

    @GetMapping("/lookOrder")
    public ResponseEntity<List<Session>> lookOrders(){
        return ResponseEntity.ok(orderService.getOrderList());
    }

    @PostMapping("/confirm")
    public String confirmOrder(Principal connectedUser){
        return orderService.confirmOrder(connectedUser);
    }
}
