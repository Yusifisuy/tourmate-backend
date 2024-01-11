package az.tourmate.services.order;

import az.tourmate.exceptions.rooms.RoomIsNotAvailableException;
import az.tourmate.exceptions.rooms.RoomIsNotFoundException;
import az.tourmate.models.order.Order;
import az.tourmate.models.order.OrderSession;
import az.tourmate.models.order.Session;
import az.tourmate.models.room.Room;
import az.tourmate.models.user.User;
import az.tourmate.repositories.order.OrderRepository;
import az.tourmate.repositories.order.SessionDao;
import az.tourmate.repositories.rooms.RoomRepository;
import az.tourmate.utils.ExceptionTexts;
import az.tourmate.utils.UserUtil;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Date;
import java.util.List;

@Service
public class OrderService {

    private final RoomRepository roomRepository;
    private final SessionDao sessionDao;

    private final OrderRepository orderRepository;
    public OrderService(RoomRepository roomRepository, SessionDao sessionDao, OrderRepository orderRepository) {
        this.roomRepository = roomRepository;
        this.sessionDao = sessionDao;
        this.orderRepository = orderRepository;
    }


    public void order(Long roomId, Integer count, boolean hasACar, Date entryDate,Date outDate){
        //TODO Rent a car integration
    }


    @Transactional
    public String order(Long roomId, Integer count, Date entryDate, Date outDate, Principal connectedUser){
        Room room = roomRepository.findByIdAndActiveIsTrue(roomId)
                .orElseThrow(()-> new RoomIsNotFoundException(ExceptionTexts.ROOM_IS_NOT_FOUND));

        User user = UserUtil.getConnectedUser(connectedUser);


        if (room.getActiveCount()>0 && room.getCapacity()>=count) {
            OrderSession orderSession = OrderSession.builder()
                    .userId(user.getId()).
                    amount(room.getRoomPrice().getPrice())
                    .roomId(roomId)
                    .entryDate(entryDate)
                    .count(count)
                    .exitDate(outDate)

                    .build();
            Session session = Session.builder().id(user.getId()).order(orderSession).build();
            sessionDao.save(session);

            return "Order was sent to tourmate for checking...";
        }
        else {
            throw new RoomIsNotAvailableException(ExceptionTexts.ROOM_IS_NOT_AVAILABLE);
        }

    }


    @Transactional
    public String confirmOrder(Principal connectedUser){
        User user = UserUtil.getConnectedUser(connectedUser);
        OrderSession  session = sessionDao.findSessionById(user.getId()).getOrder();
        Room room = roomRepository.findByIdAndActiveIsTrue(session.getRoomId())
                .orElseThrow(()-> new RoomIsNotFoundException(ExceptionTexts.ROOM_IS_NOT_FOUND));
        Order order = Order.builder().room(room).user(user)
                .amount(session.getAmount()).entryDate(session.getEntryDate())
                .exitDate(session.getExitDate()).count(session.getCount()).active(true).build();
        orderRepository.save(order);
        sessionDao.deleteSession(user.getId());
        room.setActiveCount(room.getActiveCount()-order.getCount());
        roomRepository.save(room);
        return "Ok";
    }

    public List<Session> getOrderList(){
       return sessionDao.findAll();
    }


}
