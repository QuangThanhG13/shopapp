package com.project.shopapp.services.product;

import com.project.shopapp.dtos.ProductDTO;
import com.project.shopapp.dtos.ProductImageDTO;
import com.project.shopapp.exception.DataNotFoundException;
import com.project.shopapp.exception.InvalidParamException;
import com.project.shopapp.models.Product;
import com.project.shopapp.models.ProductImage;
import com.project.shopapp.reponses.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;



public interface IProductService {
    Product craetProduct(ProductDTO productDTO) throws DataNotFoundException;
    Product getProductById(Long productId) throws DataNotFoundException;
    Product updateProduct(Long productId, ProductDTO productDTO) throws DataNotFoundException;
    void deleteProduct(Long id);
    Page<ProductResponse> getAllProducts(PageRequest pageRequest);
    boolean existsByName(String name);
    ProductImage createProductImage(Long productId,
                                           ProductImageDTO productImageDTO)
            throws DataNotFoundException, InvalidParamException;

}
