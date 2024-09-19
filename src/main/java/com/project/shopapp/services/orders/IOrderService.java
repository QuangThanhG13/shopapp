package com.project.shopapp.services.orders;

import com.project.shopapp.dtos.OrderDTO;
import com.project.shopapp.exception.DataNotFoundException;
import com.project.shopapp.models.Order;
import com.project.shopapp.reponses.OrderResponse;

import java.util.List;



public interface IOrderService {
    OrderResponse createOrder(OrderDTO orderDTO) throws DataNotFoundException;
    Order getOrderById(Long orderId);
    Order updateOrder(Long orderId, OrderDTO orderDTO);
    void deleteOrder(Long orderId);
    List<Order> findAllOrders(Long userId);


//    Page<Order> getOrderbyKeyWord(String keyWord, Pageable pageable);

}
