package com.example.demo.controller;

import com.example.demo.dto.CartRequest;
import com.example.demo.entity.CartItem;
import com.example.demo.service.CartService;
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
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@PreAuthorize("hasRole('BUYER')")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Shopping Cart", description = "Shopping cart management endpoints (Buyer only)")
public class CartController {
    
    private final CartService cartService;
    
    @GetMapping
    @Operation(summary = "Get user's cart")
    public ResponseEntity<List<CartItem>> getCart(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return ResponseEntity.ok(cartService.getCart(userId));
    }
    
    @PostMapping
    @Operation(summary = "Add item to cart")
    public ResponseEntity<CartItem> addToCart(
            @Valid @RequestBody CartRequest cartRequest,
            HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return ResponseEntity.ok(cartService.addToCart(userId, cartRequest));
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update cart item quantity")
    public ResponseEntity<CartItem> updateQuantity(
            @PathVariable Long id,
            @RequestParam Integer quantity,
            HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return ResponseEntity.ok(cartService.updateQuantity(id, quantity, userId));
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Remove item from cart")
    public ResponseEntity<Void> removeFromCart(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        cartService.removeFromCart(id, userId);
        return ResponseEntity.noContent().build();
    }
}
