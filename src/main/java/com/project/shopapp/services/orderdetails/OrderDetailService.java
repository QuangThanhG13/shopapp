//package com.project.shopapp.services.orderdetails;
//
//import com.project.shopapp.dtos.OrderDetailDTO;
//import com.project.shopapp.models.OrderDetail;
//import com.project.shopapp.repositories.OrderDetailRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//
//@Service
//@RequiredArgsConstructor
//public class OrderDetailService implements IOrderDertailService{
//
//    private final OrderDetailRepository orderDetailRepository;
//    @Override
//    public OrderDetail createOrderDetail(OrderDetailDTO newOrderDetail) {
//        OrderDetail orderDetail = new OrderDetail();
//
//        orderDetail.setColor(newOrderDetail.getColor());
//        orderDetail.setPrice(newOrderDetail.getPrice());
//        orderDetail.setTotalMoney((float) newOrderDetail.getTotalMoney());
//        orderDetail.setNumberOfProducts(newOrderDetail.getNumberOfProducts());
//
//        return orderDetailRepository.save(orderDetail);
//    }
//
//    @Override
//    public OrderDetail getOrderDetail(Long id) {
//        return orderDetailRepository.findById(id).orElse(null);
//    }
//
//    @Override
//    public OrderDetail updateOrderDetail(Long id, OrderDetailDTO newOrderDetailData) {
//
//      Optional<OrderDetail> newOrderDetail = orderDetailRepository.findById(id);
//
//      if (newOrderDetail.isPresent()) {
//          newOrderDetail.get().setColor(newOrderDetailData.getColor());
//          newOrderDetail.get().setPrice(newOrderDetailData.getPrice());
//          newOrderDetail.get().setTotalMoney((float) newOrderDetailData.getTotalMoney());
//          newOrderDetail.get().setNumberOfProducts(newOrderDetailData.getNumberOfProducts());
//      }
//        return orderDetailRepository.save(newOrderDetail.get());
//    }
//
//    @Override
//    public void deleteById(Long id) {
//       orderDetailRepository.deleteById(id);
//    }
//
//    @Override
//    public List<OrderDetail> getByOrderId(Long id) {
//        return orderDetailRepository.findByOrderId(id);
//    }
//
//}
