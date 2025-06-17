package com.example.crud.controller;

import com.example.crud.Model.Product;
import com.example.crud.repository.ProductRepository;
// PERUBAHAN: Import MultipartFile dan FileStorageService tidak lagi diperlukan untuk metode ini.
// import com.example.crud.service.FileStorageService;
// import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    // FileStorageService tidak lagi disuntikkan karena tidak digunakan
    // @Autowired
    // private FileStorageService fileStorageService;

    // Metode GET dan DELETE tidak berubah...
    @GetMapping
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        return productRepository.findById(id)
                .map(product -> {
                    productRepository.delete(product);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * PERUBAHAN: Endpoint untuk membuat produk baru.
     * Sekarang hanya menerima objek Product dalam format JSON.
     * Field 'imageUrl' sudah diisi oleh frontend.
     */
    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        // Langsung simpan objek karena imageUrl sudah ada di dalamnya.
        return productRepository.save(product);
    }

    /**
     * PERUBAHAN: Endpoint untuk memperbarui produk.
     * Sekarang hanya menerima objek Product dalam format JSON.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product productDetails) {
        return productRepository.findById(id)
                .map(existingProduct -> {
                    // Update semua field dari data yang dikirim.
                    existingProduct.setName(productDetails.getName());
                    existingProduct.setType(productDetails.getType());
                    existingProduct.setCategory(productDetails.getCategory());
                    existingProduct.setPrice(productDetails.getPrice());
                    existingProduct.setDescription(productDetails.getDescription());
                    existingProduct.setActive(productDetails.getActive());
                    // Termasuk imageUrl yang baru.
                    existingProduct.setImageUrl(productDetails.getImageUrl());
                    
                    Product updatedProduct = productRepository.save(existingProduct);
                    return ResponseEntity.ok(updatedProduct);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
 