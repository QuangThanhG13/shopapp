package com.project.shopapp.services.orderdetails;

import com.project.shopapp.dtos.OrderDetailDTO;
import com.project.shopapp.exception.DataNotFoundException;
import com.project.shopapp.models.Order;
import com.project.shopapp.models.OrderDetail;
import com.project.shopapp.models.Product;
import com.project.shopapp.repositories.OrderDetailRepository;
import com.project.shopapp.repositories.OrderRepository;
import com.project.shopapp.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderDetailService implements IOrderDertailService{

    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    @Override
    public OrderDetail createOrderDetail(OrderDetailDTO newOrderDetail) throws DataNotFoundException {
        //tim xem order'id co ton tai khong
        Order order = orderRepository.findById(newOrderDetail.getOrderId())
                .orElseThrow(() ->
                        new DataNotFoundException("Order not found with id: " + newOrderDetail.getOrderId()));
        //tim xem product'id co ton tai khong
        Product product = productRepository.findById(newOrderDetail.getProductId())
               .orElseThrow(() ->
                        new DataNotFoundException("Product not found with id: " + newOrderDetail.getProductId()));
        OrderDetail orderDetail = OrderDetail.builder()
               .order(order)
               .product(product)
//               .quantity(newOrderDetail.getQuantity())
               .color(newOrderDetail.getColor())
               .price(newOrderDetail.getPrice())
               .totalMoney((float) newOrderDetail.getTotalMoney())
               .numberOfProducts(newOrderDetail.getNumberOfProducts())
               .build();

        return orderDetailRepository.save(orderDetail);


//
//        orderDetail.setColor(newOrderDetail.getColor());
//        orderDetail.setPrice(newOrderDetail.getPrice());
//        orderDetail.setTotalMoney((float) newOrderDetail.getTotalMoney());
//        orderDetail.setNumberOfProducts(newOrderDetail.getNumberOfProducts());
//
//        return orderDetailRepository.save(orderDetail);
   }

    @Override
    public OrderDetail getOrderDetail(Long id) throws DataNotFoundException {
        return orderDetailRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Order detail not found with id: " + id));
    }

    @Override
    public OrderDetail updateOrderDetail(Long id, OrderDetailDTO newOrderDetailData) throws DataNotFoundException {
        //tim xem order Detail co ton tai khong
        OrderDetail existingOrderDetail = orderDetailRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Order detail not found with id: " + id));
//      Optional<OrderDetail> newOrderDetail = orderDetailRepository.findById(id);
        //tim kiem order co ton tai khong
        Order existingOrder = orderRepository.findById(newOrderDetailData.getOrderId())
                .orElseThrow(() -> new DataNotFoundException("Order not found with id: " + newOrderDetailData.getOrderId()));
        //tim xem product co ton tai khong
        Product existingProduct = productRepository.findById(newOrderDetailData.getProductId())
                .orElseThrow(() -> new DataNotFoundException("Product not found with id: " + newOrderDetailData.getProductId()));
        existingOrderDetail.setPrice(newOrderDetailData.getPrice());
        existingOrderDetail.setNumberOfProducts(newOrderDetailData.getNumberOfProducts());
        existingOrderDetail.setTotalMoney((float) newOrderDetailData.getTotalMoney());
        existingOrderDetail.setColor(newOrderDetailData.getColor());
        existingOrderDetail.setOrder(existingOrder);
        existingOrderDetail.setProduct(existingProduct);
        return orderDetailRepository.save(existingOrderDetail);
    }

    @Override
    public void deleteById(Long id) {
       orderDetailRepository.deleteById(id);
    }

    @Override
    public List<OrderDetail> findByOrderId(Long orderId) {
        return orderDetailRepository.findByOrderId(orderId);
    }

}
