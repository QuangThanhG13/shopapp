package com.project.shopapp.repositories;

import com.project.shopapp.models.Category;
import com.project.shopapp.models.OrderDetail;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

//    List<OrderDetail> findByOrderIdIn(Long orderId);
}
