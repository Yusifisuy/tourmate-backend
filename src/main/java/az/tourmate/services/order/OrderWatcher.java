package az.tourmate.services.order;

import az.tourmate.models.order.Order;
import az.tourmate.models.room.Room;
import az.tourmate.repositories.order.OrderRepository;
import az.tourmate.repositories.rooms.RoomRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class OrderWatcher {
    private final RoomRepository roomRepository;
    private final OrderRepository orderRepository;

    public OrderWatcher(RoomRepository roomRepository, OrderRepository orderRepository) {
        this.roomRepository = roomRepository;
        this.orderRepository = orderRepository;
    }

//    @Scheduled(cron = "0 * * * * ?")
//    public void checkOrders(){
//        Date currentDate = new Date();
//        List<Order> orders = orderRepository.findAllByExitDateAfterAndActiveIsTrue(currentDate);
//        if (orders.isEmpty()){
//            log.info("ALL CLEAR");
//        }
//        else {
//            for (Order order : orders){
//                Room room = order.getRoom();
//                room.setActiveCount(order.getCount()+room.getActiveCount());
//                order.setActive(false);
//                orderRepository.save(order);
//                roomRepository.save(room);
//                log.info("ROOM STATUS CHANGED :" + room.getId());
//            }
//        }
//
//    }
}
