package com.project.shopapp.reponses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.models.Product;
import com.project.shopapp.models.ProductImage;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    @Builder
    public class ProductResponse extends BaseResponse {
        //Đây là class giá trị trả về
//        private Long id;
        private String name;
        private Float price;
        private String thumbnail;
        private String description;
//        // Thêm trường totalPages
//        private int totalPages;

//        @JsonProperty("product_images")
//        private List<ProductImage> productImages = new ArrayList<>();

        @JsonProperty("category_id")
        private Long categoryId;
        public static ProductResponse fromProduct(Product product) {
            ProductResponse productResponse = ProductResponse.builder()
                    .name(product.getName())
                    .description(product.getDescription())
                    .thumbnail(product.getThumbnail())
                    .price(product.getPrice())
                    .categoryId(product.getCategory().getId())
                    .build();
            productResponse.setUpdatedAt(product.getUpdateAt());
            productResponse.setCreatedAt(product.getUpdateAt());
            return productResponse;
        }
}
