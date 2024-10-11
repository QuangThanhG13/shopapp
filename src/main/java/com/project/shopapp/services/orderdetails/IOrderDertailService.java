package com.project.shopapp.services.orderdetails;


import com.project.shopapp.dtos.OrderDTO;
import com.project.shopapp.dtos.OrderDetailDTO;
import com.project.shopapp.exception.DataNotFoundException;
import com.project.shopapp.models.OrderDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


public interface IOrderDertailService {
    OrderDetail createOrderDetail(OrderDetailDTO newOrderDetail) throws DataNotFoundException;
    OrderDetail getOrderDetail(Long id) throws DataNotFoundException;
    OrderDetail updateOrderDetail(Long id, OrderDetailDTO newOrderDetailData) throws DataNotFoundException;

    void deleteById(Long id);  //Xoa cung
    List<OrderDetail> findByOrderId(Long orderId);
}
