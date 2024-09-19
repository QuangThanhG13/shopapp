package com.project.shopapp.services.orders;

import com.project.shopapp.dtos.OrderDTO;
import com.project.shopapp.exception.DataNotFoundException;
import com.project.shopapp.models.Order;
import com.project.shopapp.models.OrderDetail;
import com.project.shopapp.models.OrderStatus;
import com.project.shopapp.models.User;
import com.project.shopapp.reponses.OrderResponse;
import com.project.shopapp.repositories.OrderRepository;
import com.project.shopapp.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public OrderResponse createOrder(OrderDTO orderDTO) throws DataNotFoundException {
        //Tim xem user'id co ton tai khong
        User user = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        //Convert Dto sang Entity(OrderDTO --> Order)
        //Dung thu vien Model Mapped
        //Tao 1 luong bang anh xa rieng de anh xa :
        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));
        // Cap nhap cac truong don hang tu order
        Order order = new Order();
        modelMapper.map(orderDTO, order);
        order.setUser(user);
//      order.setOrderDate(LocalDate.from(LocalDateTime.now()));
        order.setOrderDate(new Date());
        order.setStatus(OrderStatus.PROCESSING);
        //Kiem tra shippingdate phai lon hon ngay hon nay.
        LocalDate shippingDate = orderDTO.getShippingDate() == null ? LocalDate.now(): orderDTO.getShippingDate();
        if (shippingDate.isBefore(LocalDate.now())) {
            throw new DataNotFoundException("Data must be at least today");
        }
//      order.setShippingDate(LocalDate.from(LocalDateTime.now()));
        order.setActive(true);
        order.setShippingDate(shippingDate);
        order.setTotalMoney(orderDTO.getTotalMoney());
        orderRepository.save(order);
        return modelMapper.map(order,OrderResponse.class);

    }

    @Override
    public Order getOrderById(Long orderId) {
        Order seledctedOrder = orderRepository.findById(orderId).orElse(null);
        return seledctedOrder;
    }

    @Override
    public Order updateOrder(Long orderId, OrderDTO orderDTO) {
        return null;
    }

//    @Override
//    public Order updateOrder(Long orderId, OrderDTO orderDTO) {
//        // Find the existing order by ID
//        Optional<Order> optionalOrder = orderRepository.findById(orderId);
//
//        //Dung thu vien model mapped
//        modelMapper.typeMap(OrderDTO.class, Order.class)
//                .addMappings(mapper -> mapper.skip(Order::setId));
//
//        Order order = new Order();
//        modelMapper.map(orderDTO, order);
//
//        Order savedOrder = optionalOrder.get();
//
//        //save and update to order details
//       // List<OrderDetail> savedOrderDetails = orderRepository.saveAll(order.getOrderDetails());
//       // // Ensure bidirectional relationship is set(ddamr bao mqh 2 chieu )
//        for (OrderDetail savedOrderDetail : savedOrder.getOrderDetails()) {
//            savedOrderDetail.setOrder(savedOrder);
//        }
//
//        //save and update to order
//        return orderRepository.save(savedOrder);
//    }

    @Override
    public void deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }

    @Override
    public List<Order> findAllOrders(Long userId) {
//        List<Order> orders = orderRepository.findByUserId(userId);

        return null;
    }

//    @Override
//    public Page<Order> getOrderbyKeyWord(String keyWord, Pageable pageable) {
//        return orderRepository.findByKeyword(keyWord, pageable);
//
//    }
}
