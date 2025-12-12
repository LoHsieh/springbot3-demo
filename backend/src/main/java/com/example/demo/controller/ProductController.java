package com.example.demo.controller;

import com.example.demo.dto.ProductRequest;
import com.example.demo.entity.Product;
import com.example.demo.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "Product management endpoints")
public class ProductController {
    
    private final ProductService productService;
    
    @GetMapping
    @Operation(summary = "Get all products")
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }
    
    @GetMapping("/seller/my-products")
    @PreAuthorize("hasRole('SELLER')")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Get seller's products")
    public ResponseEntity<List<Product>> getMyProducts(HttpServletRequest request) {
        Long sellerId = (Long) request.getAttribute("userId");
        return ResponseEntity.ok(productService.getProductsBySeller(sellerId));
    }
    
    @PostMapping
    @PreAuthorize("hasRole('SELLER')")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Create a new product (Seller only)")
    public ResponseEntity<Product> createProduct(
            @Valid @RequestBody ProductRequest productRequest,
            HttpServletRequest request) {
        Long sellerId = (Long) request.getAttribute("userId");
        return ResponseEntity.ok(productService.createProduct(productRequest, sellerId));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SELLER')")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Update product (Seller only, own products)")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest productRequest,
            HttpServletRequest request) {
        Long sellerId = (Long) request.getAttribute("userId");
        return ResponseEntity.ok(productService.updateProduct(id, productRequest, sellerId));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SELLER')")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Delete product (Seller only, own products)")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id, HttpServletRequest request) {
        Long sellerId = (Long) request.getAttribute("userId");
        productService.deleteProduct(id, sellerId);
        return ResponseEntity.noContent().build();
    }
}
