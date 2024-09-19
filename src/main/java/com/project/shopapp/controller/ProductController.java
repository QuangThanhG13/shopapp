package com.project.shopapp.controller;

import ch.qos.logback.core.util.StringUtil;
import com.github.javafaker.Faker;
import com.project.shopapp.dtos.ProductDTO;
import com.project.shopapp.dtos.ProductImageDTO;
import com.project.shopapp.exception.DataNotFoundException;
import com.project.shopapp.models.Product;
import com.project.shopapp.models.ProductImage;
import com.project.shopapp.reponses.ProductListResponse;
import com.project.shopapp.reponses.ProductResponse;
import com.project.shopapp.services.product.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.print.Pageable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1/products" )
@RequiredArgsConstructor
public class ProductController {


    private final ProductService productService;
    @PostMapping(value = "")
    public ResponseEntity<?> createProduct(
            @Valid @RequestBody ProductDTO productDTO,
            BindingResult result) {
        // @RequestPart("file") MultipartFile file) {
        try {
            if (result.hasErrors()) {
                String errorMessages = String.valueOf(result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList());
                return ResponseEntity.badRequest().body(errorMessages);

            }
        Product newProduct = productService.craetProduct(productDTO);

            return ResponseEntity.ok(newProduct);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping(value = "upload/{id}"
            ,consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImages( @ModelAttribute ("files") List<MultipartFile> files,
                                           @PathVariable("id") Long productId,
                                           ProductDTO productDTO) throws Exception {
        try {

            Product existingProduct = productService.getProductById(productId);

            files = files == null ? new ArrayList<MultipartFile>() : files;
            if (files.size() > ProductImage.MAXIMUM_IMAGE_PER_PRODUCT) {
                return ResponseEntity.badRequest().body("You can only upload maxium 5 images");
                }
            List<ProductImage> productImages = new ArrayList<ProductImage>();

            for (MultipartFile file : files) {
                if (file.getSize() == 0) {
                    continue;
                }
                //Kiem tra kich thuoc file va dinh dang.
                if (file.getSize() > 10 * 1024 * 1024) { //kich thuoc file lon hon 10Mb
                    // throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE, "File is too large! Maximum size is 10Mb");
                    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("File is too large! Maximum size is 10Mb");
                }
                String contentType = file.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("File must be an image");
                }
                //luu file vao thu muc
                String fileName = storeFile(file);
                //sau do luu vao doi tuong product trong db
                //luu vao bang product images
                ProductImage productImage = productService.createProductImage(existingProduct.getId() ,
                        ProductImageDTO.builder()
                                .imageUrl(fileName)
                                .build());
                productImages.add(productImage);
            }
            return ResponseEntity.ok().body(productImages);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
            // Tao 1 danh sach image cho product
            // dang bi thieu product image,  an cac file name nay vao product image:
    }

    private String storeFile(MultipartFile file) throws IOException {
       if (!isImageFile(file) && file.getOriginalFilename() == null) {
           throw new IOException("Invalid image file format ");
       }

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        //Them UUID vao trc de dam bao ten file la duy nhat.
        String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;
        //duong dan den thu muc
        java.nio.file.Path uploadDir = Paths.get("upload");// co 2 duong dan path : 1 cai la doi tg path 1 cai la class path.
        //kiem tra thu muc upload ton taij chua
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        //duong dan den file moi
        java.nio.file.Path destination = Paths.get(uploadDir.toString(), uniqueFileName);

        //sao chep file vao thu muc
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFileName;
    }

    // Kiểm tra có phải file ảnh hay không
    public boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType!= null && (contentType.startsWith("image/"));
    }

    @GetMapping("")
    public ResponseEntity<ProductListResponse> getAllProducs(
            //page bắt buộc thì mình phải truyền từ 0 (page = 0)
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {
        // Tạo 1 pagable từ thông tin trạng và giới hạn
        PageRequest pageRequest = PageRequest.of(
                page, limit,
                Sort.by("createAt").descending()
        );
        // Lấy tất cả danh sách từ page request
        Page<ProductResponse> productPage = productService.getAllProducts(pageRequest);
        // tins tổng số trang
         int totalPages = productPage.getTotalPages();
         //Lấy tất cả các trang
         List<ProductResponse> products =  productPage.getContent();
        return ResponseEntity.ok(ProductListResponse.builder()
                .totalPages(totalPages)
                .products(products)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable("id") Long productId)  {
       try {
           Product existingProduct = productService.getProductById(productId);
           return ResponseEntity.ok(ProductResponse.fromProduct(existingProduct));
       } catch (Exception e) {
          return ResponseEntity.badRequest().body(e.getMessage());
       }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") long productId) {
        try {
           productService.deleteProduct(productId);
            return ResponseEntity.ok(String.format("Product deleted id = %d successfully", productId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    //Boi vi day la ham tam thoi dung de tao du lieu gia , khi nao dung thi bat
   // @PostMapping("/generateFakeDataProduct")
    private ResponseEntity<?> generateFakeDataProduct() {
        Faker faker = new Faker();
        for (int i = 0; i < 10000; i++) {
            String productName = faker.commerce().productName();
            if (productService.existsByName(productName)) {
                continue;
            }
            ProductDTO productDTO = ProductDTO.builder()
                    .name(productName)
                    .price((float) faker.number().numberBetween(2, 1000))
                    .description(faker.lorem().sentence())
                    .thumbnail("")
                    .categoryId((long) faker.number().numberBetween(3, 6))
                    .build();
            try {
                productService.craetProduct(productDTO);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
        return ResponseEntity.ok("Generate fake data successfully");
    }
}





/*
{
    "name" : "xx",
    "price": 812.3,
    "thumbnail": "",
    "decription": "THis is best seller at shop ",
    "category_id": 1
}
 */