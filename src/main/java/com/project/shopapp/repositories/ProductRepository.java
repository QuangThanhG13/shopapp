package com.project.shopapp.repositories;

import com.project.shopapp.models.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.domain.Pageable;


public interface ProductRepository extends JpaRepository<Product,Long> {

    boolean existsByName(String name);

  public abstract  Page<Product> findAll(Pageable pageable);
}
