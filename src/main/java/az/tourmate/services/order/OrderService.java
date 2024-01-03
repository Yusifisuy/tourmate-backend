package az.tourmate.services.order;

import az.tourmate.exceptions.rooms.RoomIsNotAvailableException;
import az.tourmate.exceptions.rooms.RoomIsNotFoundException;
import az.tourmate.models.order.Order;
import az.tourmate.models.order.Session;
import az.tourmate.models.room.Room;
import az.tourmate.models.user.User;
import az.tourmate.repositories.dao.SessionDao;
import az.tourmate.repositories.rooms.RoomRepository;
import az.tourmate.utils.ExceptionTexts;
import az.tourmate.utils.UserUtil;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Date;

@Service
public class OrderService {

    private final RoomRepository roomRepository;
    private final SessionDao sessionDao;


    public OrderService(RoomRepository roomRepository, SessionDao sessionDao) {
        this.roomRepository = roomRepository;
        this.sessionDao = sessionDao;
    }


    public void order(Long roomId, Integer count, boolean hasACar, Date entryDate,Date outDate){
        //TODO Rent a car integration
    }


    @Transactional
    public String order(Long roomId, Integer count, Date entryDate, Date outDate, Principal connectedUser){
        Room room = roomRepository.findByIdAndActiveIsTrue(roomId)
                .orElseThrow(()-> new RoomIsNotFoundException(ExceptionTexts.ROOM_IS_NOT_FOUND));

        User user = UserUtil.getConnectedUser(connectedUser);


        if (room.getActiveCount()>0 && room.getCapacity()<=count) {
            Order order = Order.builder().user(user).room(room).entryDate(entryDate).exitDate(outDate).build();
            sessionDao.save(Session.builder().order(order).build());

            return "Order was sent to tourmate for checking...";
        }
        else {
            throw new RoomIsNotAvailableException(ExceptionTexts.ROOM_IS_NOT_AVAILABLE);
        }


    }


}
