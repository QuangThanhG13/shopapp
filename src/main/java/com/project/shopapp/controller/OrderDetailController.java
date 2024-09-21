package com.project.shopapp.controller;

import com.project.shopapp.dtos.OrderDetailDTO;
import com.project.shopapp.exception.DataNotFoundException;
import com.project.shopapp.models.OrderDetail;
import com.project.shopapp.reponses.OrderDetailResponse;
import com.project.shopapp.services.orderdetails.OrderDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order_detail")
@RequiredArgsConstructor
public class OrderDetailController {
    private final OrderDetailService orderDetailService;

    @PostMapping("")
    public ResponseEntity<?> createOrderDetail(@Valid @RequestBody OrderDetailDTO orderDetailDTO) {
        try {
            OrderDetail orderDetail = orderDetailService.createOrderDetail(orderDetailDTO);
            return ResponseEntity.ok(OrderDetailResponse.fromOrderDetail(orderDetail));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetail(@Valid @PathVariable("id") Long id) throws DataNotFoundException {
        return ResponseEntity.ok(OrderDetailResponse.fromOrderDetail(orderDetailService.getOrderDetail(id)));
//        return ResponseEntity.ok(orderDetailService.getOrderDetail(id));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getOrderDetails(@Valid @PathVariable("orderId") Long orderId) {
          List<OrderDetail> orderDetails = orderDetailService.findByOrderId(orderId);
          List<OrderDetailResponse> orderDetailResponses = orderDetails.stream().map(orderDetail -> OrderDetailResponse.fromOrderDetail(orderDetail)).toList();
        return ResponseEntity.ok(orderDetailResponses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderDetail(@Valid @PathVariable("id") Long id,
                                              @Valid @RequestBody OrderDetailDTO orderDetailData) {
      try {
         OrderDetail orderDetail = orderDetailService.updateOrderDetail(id, orderDetailData);
          return ResponseEntity.ok(OrderDetailResponse.fromOrderDetail(orderDetail) );
      } catch (DataNotFoundException e) {
          return ResponseEntity.badRequest().body(e.getMessage());
      }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderDetail(@Valid @PathVariable("id") Long id) {
        orderDetailService.deleteById(id);
        return ResponseEntity.ok("Delete order detail with id " + id);
    }



}
