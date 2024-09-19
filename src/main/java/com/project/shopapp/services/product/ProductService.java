package com.project.shopapp.services.product;

import com.project.shopapp.dtos.OrderDTO;
import com.project.shopapp.dtos.ProductDTO;
import com.project.shopapp.dtos.ProductImageDTO;
import com.project.shopapp.exception.DataNotFoundException;
import com.project.shopapp.exception.InvalidParamException;
import com.project.shopapp.models.*;
import com.project.shopapp.reponses.ProductResponse;
import com.project.shopapp.repositories.CategoryRepository;
import com.project.shopapp.repositories.ProductImageRepository;
import com.project.shopapp.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService{
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;
    private  ModelMapper modelMapper;


    @Override
    public Product craetProduct(ProductDTO productDTO) throws DataNotFoundException {
        //kiem tra xem category co ton tai khong
        //b1: kiểm tra
      Category existingCategory =  categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new DataNotFoundException(
                        "Can not found category with id" + productDTO.getCategoryId()));

        //C1 : Su dung buider
        Product newProduct = Product.builder()
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .thumbnail(productDTO.getThumbnail())
                .description(productDTO.getDescription())
                .category(existingCategory)
                .build();


//        //C2 : Convert Dto sang Entity(OrderDTO --> Order)
//        //Dung thu vien Model Mapped
//        //Tao 1 luong bang anh xa rieng de anh xa :
//        modelMapper.typeMap(ProductDTO.class, Product.class)
//                .addMappings(mapper -> mapper.skip(Product::setId));
//        //Cap nhap cac truong tu product
//        Product product = new Product();
//        modelMapper.map(productDTO, product);
//        //luu vao csdl

        return productRepository.save(newProduct);
    }

    @Override
    public Product getProductById(Long productId) throws DataNotFoundException {
        return productRepository.findById(productId)
                .orElseThrow(() -> new DataNotFoundException("Can not find product with id " + productId))  ;
    }

    @Override
    public Product updateProduct(Long productId, ProductDTO productDTO) throws DataNotFoundException {
        Product opinalProduct = productRepository.findById(productId)
                .orElseThrow(() -> new DataNotFoundException("Can not find product with id " + productId));

        Product existingProduct = opinalProduct;

        //C1 : Khong dung thu vien set tay
        if (existingProduct != null) {
            //kiem tra xem category id co ton tai khong
            Category existingCategory =  categoryRepository.findById(productDTO.getCategoryId())
                    .orElseThrow(() -> new DataNotFoundException(
                            "Can not found category with id" + productDTO.getCategoryId()));
            //copy cac thuoc tinh tu DTO sang product
            existingProduct.setName(productDTO.getName());
            existingProduct.setPrice(productDTO.getPrice());
            existingProduct.setThumbnail(productDTO.getThumbnail());
//            existingProduct.setCategoryId(productDTO.getCategoryId());
            //existingProduct.setCategory(existingCategory);
            existingProduct.setDescription(productDTO.getDescription());
            return productRepository.save(existingProduct);
        }
        return null;

//         //C2 : Dung thu vien Moder Mapped
//        modelMapper.typeMap(ProductDTO.class, Product.class)
//               .addMappings(mapper -> mapper.skip(Product::setId));
//        // Cap nhap cac truong tu product
//        modelMapper.map(productDTO, existingProduct);

        // If you have a bidirectional relationship, ensure it's properly set
        // This block is only necessary if Product has a collection of related entities
        // Example: If Product has a list of related entities (e.g., `categories` or `orderDetails`)
    /*
    for (RelatedEntity relatedEntity : existingProduct.getRelatedEntities()) {
        relatedEntity.setProduct(existingProduct);  // Set the relationship if needed
    }
    */
        //luu vao csdl
        // Save and return the updated product

    }
    @Override
    public void deleteProduct(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        optionalProduct.ifPresent(productRepository::delete);  // dungf bieeu thuc lamba

// c2:        if(optionalProduct.isPresent()) {
//            productRepository.delete(optionalProduct.get());
//        }


    }

    @Override
    public Page<ProductResponse> getAllProducts(PageRequest pageRequest) {
       //lay san pham tu page den limit
        return productRepository.findAll(pageRequest).map(product -> ProductResponse.fromProduct(product));
    }

    @Override
    public boolean existsByName(String name) {
        return false;
    }

    @Override
    public ProductImage createProductImage(Long productId,
                                           ProductImageDTO productImageDTO)
            throws DataNotFoundException, InvalidParamException {

        Product existingProduct = productRepository.findById(productId)
               .orElseThrow(() -> new DataNotFoundException("Can not find product with id " + productId));

        ProductImage newProductImage = ProductImage.builder()
                .imageUrl(productImageDTO.getImageUrl())
                .product(existingProduct)
                .build();
        //Khong cho isert qua 5 anh cho 1 san pham
        int size = productImageRepository.findByProductId(productId).size();
        if (size >= ProductImage.MAXIMUM_IMAGE_PER_PRODUCT ) {
            throw new InvalidParamException("Number of images must be < 5");
        }
     return   productImageRepository.save(newProductImage);

    }
}
